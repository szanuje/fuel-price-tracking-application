async function getNominatimData(lat, lng) {
    return await fetchJSON(
        'https://nominatim.openstreetmap.org/reverse?format=jsonv2&lat=' +
        lat + '&lon=' + lng
    );
}

async function getOSMData(osm_type, osm_id) {
    return await fetchJSON(
        'http://overpass-api.de/api/interpreter?data=[out:json];' +
        osm_type + '(' + osm_id + ');out%20meta;'
    );
}

function getPlaceTags(osm_data) {
    return osm_data.elements[0].tags;
}

function getPlaceAddress(nominatim_data) {
    return nominatim_data.address;
}

mapboxgl.accessToken = 'pk.eyJ1IjoibWp1ZG5hdXN6eSIsImEiOiJjazdyazV6am8wZHRhM2xwbHljeTAxdnlxIn0.xd5-kSDoh1hODWmdjfuB8A';
/* init map */
var mymap = new mapboxgl.Map({
    interactive: true,
    trackResize: true,
    zoomControl: false,
    pitchWithRotate: false,
    dragRotate: false,
    touchZoomRotate: false,
    attributionControl: false,
    container: 'map',
    style: 'mapbox://styles/mapbox/streets-v11',
    center: [19.93673, 50.05844],
    zoom: 15
});

var geocoder = new MapboxGeocoder({
    accessToken: mapboxgl.accessToken,
    mapboxgl: mapboxgl,
    placeholder: 'Find Station',
    countries: 'pl',
    marker: false,
    clearOnBlur: true,
    limit: 5,
    zoom: 17
});

var navigation = new mapboxgl.NavigationControl({
    showCompass: false
});

mymap.on('load', function () {
    mymap
        .addControl(navigation, 'top-left')
        .addControl(geocoder, 'top-left');
    /* set blur and pointer resistance */
    $('.mapboxgl-canvas').addClass('no-pointer blur');
    $('.mapboxgl-ctrl.mapboxgl-ctrl-group').addClass('no-pointer blur');
});

var marker_div = document.createElement('div');
marker_div.className = 'marker';
var myMarker = new mapboxgl.Marker(marker_div);

myMarker
    .setLngLat(mymap.getCenter())
    .addTo(mymap);

mymap.on('move', function () {
    myMarker.setLngLat(mymap.getCenter());
    let position = myMarker.getLngLat();
    $('#lat').val(position.lat);
    $('#lng').val(position.lng);
});

mymap.on('moveend', async function () {
    let coords = mymap.getCenter();

    console.group('Fill table with station info');
    console.time('time elapsed');
    let nominatim_data = await getNominatimData(coords.lat, coords.lng);
    let osm_data = await getOSMData(nominatim_data.osm_type, nominatim_data.osm_id);
    console.log(nominatim_data);
    console.log(osm_data);
    console.timeEnd('time elapsed');
    console.groupEnd();

    $('.station-info').removeClass('hide');
    let address = getPlaceAddress(nominatim_data);
    let tags = getPlaceTags(osm_data);

    let street = [address.road, address.city].find(isNotNullNorUndefined);
    let city = [address.village, address.town, address.city].find(isNotNullNorUndefined);
    if (address.house_number != null) {
        street = street + ' ' + address.house_number;
    }
    let postcode = address.postcode;
    let amenity = tags.amenity;
    let name = tags.name != null ? tags.name : tags['name:en'];

    $('#station-city').val(city);
    $('#station-postalcode').val(postcode);
    $('#station-street').val(street);
    $('#station-name').val(name);
    $('#amenity').val(amenity);

    /* handle station form */
    if (amenity !== 'fuel' && amenity != null) {
        $('#submit-station').attr('type', 'button');
        $('#station-warning').removeClass('hide');
    } else {
        $('#submit-station').attr('type', 'submit');
        $('#station-warning').addClass('hide');
    }
});

mymap.once('movestart', function () {
    $('.mapboxgl-canvas').removeClass('no-pointer blur');
    $('.mapboxgl-ctrl.mapboxgl-ctrl-group').removeClass('no-pointer blur');
});