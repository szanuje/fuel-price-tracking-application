document.addEventListener("DOMContentLoaded", function (event) {

    /* detects changes */
    var observeDOM = (function () {
        var MutationObserver = window.MutationObserver || window.WebKitMutationObserver;

        return function (obj, callback) {
            if (!obj || !obj.nodeType === 1) return; // validation

            if (MutationObserver) {
                // define a new observer
                var obs = new MutationObserver(function (mutations, observer) {
                    callback(mutations);
                })
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

    /* get JSON from URL */
    async function fetchJSON(url, init) {
        const response = await fetch(url, init);
        if (!response.ok) {
            throw new Error("HTTP error " + response.status);
        }
        return response.json();
    }

    let loginButton = $('#menulogin');
    let registerButton = $('#menuregister');

    // handle pop-up modals
    function show(event, modal) {
        let blurDiv = $('#blur-me');
        event.stopImmediatePropagation();
        modal.removeClass('hide');
        blurDiv.addClass('blur');
        blurDiv.addClass('no-pointer');
        $('html').on('click', function (e) {
            hide(e, modal);
        });
    }

    function hide(ev, modal) {
        let blurDiv = $('#blur-me');
        if (!$(ev.target).is(modal) && !$(modal).has(ev.target).length > 0) {
            modal.addClass('hide');
            blurDiv.removeClass('blur');
            blurDiv.removeClass('no-pointer');
            $('html').off();
        }
    }

    function handleModals() {
        let loginModal = $('#login-modal');
        let registerModal = $('#register-modal');

        loginButton.on('click', function (e) {
            show(e, loginModal);
        });

        registerButton.on('click', function (e) {
            show(e, registerModal);
        });
    }

    handleModals();

    // handle warning messages on modals
    function handleModalWarnings() {
        let url = window.location.href;

        if (url.endsWith("?login=false")) {
            loginButton.click();
            document.getElementById('wrong-username-warning').style.display = 'block';
            history.replaceState({}, document.title, url.split('?')[0]);  // replace / with . to keep url
        }

        if (url.endsWith("?login=true")) {
            loginButton.click();
            history.replaceState({}, document.title, url.split('?')[0]);  // replace / with . to keep url
        }

        if (url.endsWith("?register=false")) {
            registerButton.click();
            document.getElementById('user-exists-warning').style.display = 'block';
            history.replaceState({}, document.title, url.split('?')[0]);  // replace / with . to keep url
        }
    }

    handleModalWarnings();

    //handle register process
    function validatePassword(event, pass, confirmPass) {

        if (pass.value !== confirmPass.value) {
            document.getElementById('password-match-warning').style.display = 'block';
            document.getElementById('registerbutton').setAttribute('type', 'button');
            event.preventDefault();
        } else {
            document.getElementById('password-match-warning').style.display = 'none';
            document.getElementById('registerbutton').setAttribute('type', 'submit');
        }
    }

    function handleRegisterProcess() {
        let password_input = document.getElementById('pass');
        let confirm_password_input = document.getElementById('confirm-pass');
        password_input.onkeyup = function (event) {
            validatePassword(event, password_input, confirm_password_input);
        };
        confirm_password_input.onkeyup = function (event) {
            validatePassword(event, password_input, confirm_password_input);
        };
    }

    handleRegisterProcess();

    /* init map */
    mapboxgl.accessToken = 'pk.eyJ1IjoibWp1ZG5hdXN6eSIsImEiOiJjazdyazV6am8wZHRhM2xwbHljeTAxdnlxIn0.xd5-kSDoh1hODWmdjfuB8A';

    var mymap = new mapboxgl.Map({
        interactive: true,
        trackResize: true,
        zoomControl: false,
        pitchWithRotate: false,
        dragRotate: false,
        touchZoomRotate: false,
        container: 'main-map',
        style: 'mapbox://styles/mapbox/streets-v11',
        center: [19.93673, 50.05844],
        zoom: 13
    });

    var geocoder = new MapboxGeocoder({
        accessToken: mapboxgl.accessToken,
        mapboxgl: mapboxgl,
        placeholder: 'Your Location',
        countries: 'pl',
        marker: false
    });

    var navigation = new mapboxgl.NavigationControl({
        showCompass: false
    });

    var myCircle = new MapboxCircle(mymap.getCenter(), 1000, {
        editable: true,
        minRadius: 1,
        maxRadius: 15000,
        fillColor: '#29AB87',
        strokeColor: '#005a8c',
        strokeWeight: 20,
        strokeOpacity: 0.01
    });

    var myMarker = new mapboxgl.Marker({
        color: '#000000'
    });

    // initialize the map canvas to interact with later
    var canvas = mymap.getCanvasContainer();

    // create a function to make a directions request
    function getRoute(end) {
        // make directions request using driving profile
        var url =
            'https://api.mapbox.com/directions/v5/mapbox/driving/' +
            mymap.getCenter().lng +
            ',' +
            mymap.getCenter().lat +
            ';' +
            end[0] +
            ',' +
            end[1] +
            '?steps=true&geometries=geojson&access_token=' +
            mapboxgl.accessToken;

        var req = new XMLHttpRequest();
        req.open('GET', url, true);
        req.onload = function () {
            var json = JSON.parse(req.response);
            var data = json.routes[0];
            var route = data.geometry.coordinates;
            var geojson = {
                'type': 'Feature',
                'properties': {},
                'geometry': {
                    'type': 'LineString',
                    'coordinates': route
                }
            };
            if (mymap.getLayer("route")) {
                mymap.removeLayer("route");
            }
            if (mymap.getSource("route")) {
                mymap.removeSource("route");
            }

            mymap.addLayer({
                'id': 'route',
                'type': 'line',
                'source': {
                    'type': 'geojson',
                    'data': {
                        'type': 'Feature',
                        'properties': {},
                        'geometry': {
                            'type': 'LineString',
                            'coordinates': {
                                'type': 'Feature',
                                'properties': {},
                                'geometry': {
                                    'type': 'LineString',
                                    'coordinates': route
                                }
                            }
                        }
                    }
                },
                'layout': {
                    'line-join': 'round',
                    'line-cap': 'round'
                },
                'paint': {
                    'line-color': '#3887be',
                    'line-width': 6,
                    'line-opacity': 0.75
                }
            });

            mymap.getSource('route').setData(geojson);

            // get the sidebar and add the instructions
            var instructions = document.getElementById('instructions');
            var steps = data.legs[0].steps;

            var tripInstructions = [];
            for (var i = 0; i < steps.length; i++) {
                tripInstructions.push('<br><li>' + steps[i].maneuver.instruction) +
                '</li>';
                instructions.innerHTML =
                    '<span class="trip-info">' +
                    (data.distance / 1000).toFixed(1) + 'km | ' + Math.floor(data.duration / 60) + 'min</span>' +
                    tripInstructions;
            }
            instructions.classList.remove('hide');
        };
        req.send();
    }

    mymap.on('load', function () {
        mymap
            .addControl(navigation, 'top-left')
            .addControl(geocoder, 'top-left');
    });

    // allow the user to click the map to change the destination
    function setDestinationPoint(lng, lat) {
        canvas.style.cursor = '';
        var coords = [lng, lat];
        var end = {
            'type': 'FeatureCollection',
            'features': [
                {
                    'type': 'Feature',
                    'properties': {},
                    'geometry': {
                        'type': 'Point',
                        'coordinates': coords
                    }
                }
            ]
        };
        if (mymap.getLayer('end')) {
            mymap.getSource('end').setData(end);
        } else {
            mymap.addLayer({
                'id': 'end',
                'type': 'circle',
                'source': {
                    'type': 'geojson',
                    'data': {
                        'type': 'FeatureCollection',
                        'features': [
                            {
                                'type': 'Feature',
                                'properties': {},
                                'geometry': {
                                    'type': 'Point',
                                    'coordinates': coords
                                }
                            }
                        ]
                    }
                },
                'paint': {
                    'circle-radius': 10,
                    'circle-color': '#0099cc'
                }
            });
        }
        getRoute(coords);
        markers_list.forEach(marker => {
            if (marker.getLngLat().lat === lat && marker.getLngLat().lng === lng) {
                marker.getPopup().addTo(mymap);
            }
        });
    }

    myMarker
        .setLngLat(mymap.getCenter())
        .addTo(mymap);

    myCircle.addTo(mymap);

    async function updateMarkers(lat, lng, distance, markers_list, map) {
        let table = document.getElementById('stations-list');
        if (table != null) table.remove();

        let data = await fetchJSON('http://localhost:8080/get-markers?lat=' + lat + '&lon=' + lng + '&distance=' + distance);

        if (data.length > 0) {
            table = generateTableSkeleton();
            document.getElementById('site').appendChild(table);

            data.forEach((item) => {

                let popup = new mapboxgl.Popup({
                    offset: 25,
                    closeButton: true,
                    closeOnClick: true
                }).setHTML(
                    item.name + '<br>' + item.street
                );

                let temp_marker = new mapboxgl.Marker({
                    color: '#0099cc'
                })
                    .setLngLat([item.lon, item.lat])
                    .setPopup(popup)
                    .addTo(map);

                markers_list.push(temp_marker);
                populateTable(table, item);
            });

            sortTable();
            $('#stations-list').find('tbody:nth-child(n+2)').on('click', receiveCoordsOnTableclick);
        }

    }

    function receiveCoordsOnTableclick() {
        let lng = $(this).find('td:nth-child(6)').text();
        let lat = $(this).find('td:nth-child(7)').text();
        setDestinationPoint(Number(lng), Number(lat));
    }

    function clearMarkers(markers_list) {
        markers_list.forEach((marker) => marker.remove());
        markers_list.length = 0;
    }

    var markers_list = [];

    myCircle.on('radiuschanged', async function (circleObj) {
        let coords = mymap.getCenter();
        clearMarkers(markers_list);
        await updateMarkers(coords.lat, coords.lng, circleObj.getRadius(), markers_list, mymap);
    });

    mymap.on('dragend', async function () {
        let origin = mymap.getCenter();
        clearMarkers(markers_list);
        await updateMarkers(origin.lat, origin.lng, myCircle.getRadius(), markers_list, mymap);
    });

    mymap.on('move', function () {
        myMarker.setLngLat(mymap.getCenter());
        myCircle.setCenter(mymap.getCenter());
    });

    /* sort tables */
    function sortTable() {
        const getCellValue = (tr, idx) => tr.children[idx].innerText || tr.children[idx].textContent;

        const comparer = (idx, asc) => (a, b) => ((v1, v2) =>
                v1 !== '' && v2 !== '' && !isNaN(v1) && !isNaN(v2) ? v1 - v2 : v1.toString().localeCompare(v2)
        )(getCellValue(asc ? a : b, idx), getCellValue(asc ? b : a, idx));

        // do the work...
        document.querySelectorAll('th').forEach(th => th.addEventListener('click', (() => {
            const table = th.closest('table');
            Array.from(table.querySelectorAll('tbody:nth-child(n+2) > tr'))
                .sort(comparer(Array.from(th.parentNode.children).indexOf(th), this.asc = !this.asc))
                .forEach(tr => table.appendChild(tr.parentNode));
        })));
    }

    /* handle dynamic table with prices */
    function populateTable(table, data) {

        let html = [
            '            <tr>\n' +
            '                <td>\n' +
            '                    <ul>\n' +
            '                        <li>' + data.name + '</li>\n' +
            '                        <li>' + data.street + '</li>\n' +
            '                    </ul>\n' +
            '                </td>\n' +
            '                <td>' + data.prices[0].pb95 + '</td>\n' +
            '                <td>' + data.prices[0].pb98 + '</td>\n' +
            '                <td>' + data.prices[0].lpg + '</td>\n' +
            '                <td>' + data.prices[0].diesel + '</td>\n' +
            '                <td style="display: none;">' + data.lon + '</td>\n' +
            '                <td style="display: none;">' + data.lat + '</td>\n' +
            '            </tr>'
        ].join('\n');
        table.insertAdjacentHTML('beforeend', html);
    }

    function generateTableSkeleton() {
        let table = document.createElement('table');
        table.id = 'stations-list';
        let titles = document.createElement('tr');

        let station_title = document.createElement('th');
        station_title.innerHTML = 'Station';
        let pb95_title = document.createElement('th');
        pb95_title.innerHTML = 'Pb 95 <i class="fas fa-sort"></i>';
        let pb98_title = document.createElement('th');
        pb98_title.innerHTML = 'Pb 98 <i class="fas fa-sort"></i>';
        let lpg_title = document.createElement('th');
        lpg_title.innerHTML = 'LPG <i class="fas fa-sort"></i>';
        let diesel_title = document.createElement('th');
        diesel_title.innerHTML = 'Diesel <i class="fas fa-sort"></i>';
        let lng_title = document.createElement('th');
        lng_title.innerHTML = 'Longitude';
        lng_title.style.display = 'none';
        let lat_title = document.createElement('th');
        lat_title.innerHTML = 'Latitude';
        lat_title.style.display = 'none';

        titles.appendChild(station_title);
        titles.appendChild(pb95_title);
        titles.appendChild(pb98_title);
        titles.appendChild(lpg_title);
        titles.appendChild(diesel_title);
        titles.appendChild(lng_title);
        titles.appendChild(lat_title);
        table.appendChild(titles);
        return table;
    }

});
