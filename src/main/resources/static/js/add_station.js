document.addEventListener("DOMContentLoaded", function (event) {

    /* detects changes */
    var observeDOM = (function () {
        let MutationObserver = window.MutationObserver || window.WebKitMutationObserver;

        return function (obj, callback) {
            if (!obj || !obj.nodeType === 1) return; // validation

            if (MutationObserver) {
                // define a new observer
                let obs = new MutationObserver(function (mutations, observer) {
                    callback(mutations);
                });
                // have the observer observe foo for changes in children
                obs.observe(obj, {childList: true, subtree: true});
            } else if (window.addEventListener) {
                obj.addEventListener('DOMNodeInserted', callback, false);
                obj.addEventListener('DOMNodeRemoved', callback, false);
            }
        }
    })();

    /* check if element exists */
    const checkElement = async selector => {

        while (document.querySelector(selector) === null) {
            await new Promise(resolve => requestAnimationFrame(resolve))
        }

        return document.querySelector(selector);
    };

    // checkElement('.leaflet-control-zoom')
    //     .then((element) => {
    //         $(element).addClass('blur no-pointer');
    //     });

    /* geocoding */
    async function fetchJSON(url, init) {
        const response = await fetch(url, init);
        if (!response.ok) {
            throw new Error("HTTP error " + response.status);
        }
        return response.json();
    }

    async function getNominatimData(lat, lng) {
        return await fetchJSON(
            'https://nominatim.openstreetmap.org/reverse?format=jsonv2&lat=' + lat + '&lon=' + lng
        );
    }

    async function getOSMData(osm_type, osm_id) {
        // return jQuery.get('https://nominatim.openstreetmap.org/reverse?format=jsonv2&lat=' + lat + '&lon=' + lng)
        //     .then(function (data) {
        //         return jQuery.get('http://overpass-api.de/api/interpreter?data=[out:json];way(' + data.osm_id + ');out;')
        //             .then(function (data2) {
        //                 return data2;
        //             });
        //     });
        return await fetchJSON(
            'http://overpass-api.de/api/interpreter?data=[out:json];' + osm_type + '(' + osm_id + ');out;'
        );
    }

    function getPlaceTags(osm_data) {
        return osm_data.elements[0].tags;
    }

    function getPlaceAddress(nominatim_data) {
        return nominatim_data.address;
    }

    /* init map */
    var mymap = L.map('map').setView([50.06331, 19.937439], 13);

    L.tileLayer('https://api.mapbox.com/styles/v1/{id}/tiles/{z}/{x}/{y}?access_token={accessToken}', {
        // attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors, <a href="https://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
        maxZoom: 18,
        id: 'mapbox/streets-v11',
        tileSize: 512,
        zoomOffset: -1,
        accessToken: 'pk.eyJ1IjoibWp1ZG5hdXN6eSIsImEiOiJjazdya2F6MXYwN3V3M2xtcjhhY2plY3NvIn0.erTiKL-XmN43C62LmMxykA'
    }).addTo(mymap);

    var markerIcon = L.icon({
        iconUrl: '/images/marker.png',
        iconSize: [40, 55],
        iconAnchor: [15, 55],
        popupAnchor: [0, -55],
    });

    var myMarker = L.marker([0, 0], {
        title: "MyPoint",
        alt: "Point",
        draggable: false
    })
        .setIcon(markerIcon)
        .setOpacity(0.7)
        .addTo(mymap);
    // .on('dragend', function () {
    // });

    mymap.on('move', function () {
        myMarker.setLatLng(mymap.getCenter());
        let position = myMarker.getLatLng();
        lat = position.lat;
        lng = position.lng;
        myMarker.bindPopup(lat + ', ' + lng);
        $('#lat').val(lat);
        $('#lng').val(lng);
    });

    var old_zoom = mymap.getZoom();
    mymap.on('moveend', async function () {
        let coords = myMarker.getLatLng();

        if (old_zoom === mymap.getZoom()) {
            let nominatim_data = await getNominatimData(coords.lat, coords.lng);
            let osm_data = await getOSMData(nominatim_data.osm_type, nominatim_data.osm_id);
            let address = await getPlaceAddress(nominatim_data);
            let tags = await getPlaceTags(osm_data);

            let street;
            let city;

            if (address.village != null) {
                city = address.village;
                street = city + ' ' + address.road;
            }
            if (address.city != null) {
                city = address.city;
                let road = address.road != null ? address.road : city;
                street = road + ' ' + address.house_number;
            }
            let postcode = address.postcode;
            let amenity = tags.amenity;
            let name = tags.name != null ? tags.name : tags['name:en'];

            $('#station-city').val(city);
            $('#station-postalcode').val(postcode);
            $('#station-street').val(street);
            $('#station-name').val(name);
            $('#amenity').val(amenity);

            // console.log(osm_data);

            /* handle station form */
            if (amenity !== 'fuel' && amenity != null) {
                $('#submit-station').attr('type', 'button');
            } else {
                $('#submit-station').attr('type', 'submit');
            }
        } else {
            old_zoom = mymap.getZoom();
        }
    });

    var search_control = new L.Control.Search({
        url: 'https://nominatim.openstreetmap.org/search?format=json&q={s}',
        position: 'topleft',
        jsonpParam: 'json_callback',
        propertyName: 'display_name',
        propertyLoc: ['lat', 'lon'],
        marker: myMarker,
        autoCollapse: true,
        autoType: false,
        minLength: 2
    });

    mymap.addControl(search_control);

    /* set blur and pointer resistance */
    $('#map').addClass('no-pointer');
    $('.leaflet-pane').addClass('blur');
    $('.leaflet-control-zoom').addClass('blur no-pointer');

    /* remove blur and poniter resistance when location is selected */
    observeDOM(document.querySelector('.search-tooltip'), function () {
        document.querySelectorAll('.search-tip').forEach(item => {
            item.addEventListener("click", event => {
                console.log('clicked on .search-tip / removing blur and pointer resistance');
                $('#map').removeClass('no-pointer');
                $('.leaflet-pane').removeClass('blur');
                $('.leaflet-control-zoom').removeClass('blur no-pointer');
                console.log(myMarker.getLatLng().toString())

            });
        });
    });

});