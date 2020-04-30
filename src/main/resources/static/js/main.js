mapboxgl.accessToken = 'pk.eyJ1IjoibWp1ZG5hdXN6eSIsImEiOiJjazdyazV6am8wZHRhM2xwbHljeTAxdnlxIn0.xd5-kSDoh1hODWmdjfuB8A';

var mymap = new mapboxgl.Map({
    interactive: true,
    trackResize: true,
    zoomControl: false,
    pitchWithRotate: false,
    dragRotate: false,
    touchZoomRotate: false,
    attributionControl: false,
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
    marker: false,
    clearOnBlur: true,
    limit: 5,
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

var nearbyMarkersAndStations = new Map();
var currentMarker;
var currentStation;
var myChart;

// create a function to make a directions request
async function getRoute(end) {
    // make directions request using driving profile
    let url =
        'https://api.mapbox.com/directions/v5/mapbox/driving/' +
        mymap.getCenter().lng + ',' + mymap.getCenter().lat + ';' +
        end[0] + ',' + end[1] +
        '?steps=true&geometries=geojson&access_token=' +
        mapboxgl.accessToken;

    const json = await fetchJSON(url);
    let data = json.routes[0];
    let route = data.geometry.coordinates;
    let geojson = {
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
            'line-color': '#0099cc',
            'line-width': 6,
            'line-opacity': 0.75
        }
    });

    mymap.getSource('route').setData(geojson);

    // get the sidebar and add the instructions
    let instructions = document.getElementById('instructions');
    fillRouteInstructions(instructions, data);
}

function fillRouteInstructions(element, data) {
    let steps = data.legs[0].steps;

    let dir = '';
    let icon = '';
    let tripInstructions = [];

    for (let i = 0; i < steps.length; i++) {
        dir = steps[i].maneuver.instruction;
        icon = getDirectionImage(dir);
        tripInstructions += '<li style="list-style-image: url(' + icon + ');">' + dir + '</li>';
    }

    element.innerHTML =
        '<span class="trip-info">' +
        (data.distance / 1000).toFixed(1) + 'km | ' +
        Math.floor(data.duration / 60) + 'min</span>' +
        '<br>' +
        tripInstructions;

    element.classList.remove('hide');
    $('#instructions').css('display', '');
}

function getDirectionImage(maneuver) {
    let dir = maneuver;
    if (dir.includes('Head')) {
        return '/images/start.png';
    } else if (dir.includes('arrived')) {
        return '/images/finish.png';
    } else if (dir.includes('U-turn')) {
        return '/images/u-turn.png';
    } else if (dir.includes('right')) {
        if (dir.includes('slight')) {
            return '/images/bear-right.png';
        }
        return '/images/right.png';
    } else if (dir.includes('left')) {
        if (dir.includes('slight')) {
            return '/images/bear-left.png';
        }
        return '/images/left.png';
    } else if (dir.includes('Enter')) {
        return '/images/roundabout.png';
    } else {
        return '/images/error.png';
    }
}

// allow the user to click the map to change the destination
async function setDestinationAndShowRoute(lng, lat) {
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
                'circle-radius': 3,
                'circle-color': '#0099cc'
            }
        });
    }
    await getRoute(coords);
}

function findMarkerAndOpenPopup(nearbyMarkersAndStations, lat, lng) {

    for (const marker of nearbyMarkersAndStations.keys()) {
        if (marker.getLngLat().lat === Number(lat) && marker.getLngLat().lng === Number(lng)) {
            if (currentMarker !== undefined) {
                currentMarker.getPopup().remove();
            }

            currentMarker = marker;
            currentStation = nearbyMarkersAndStations.get(marker);
            currentMarker.getPopup().addTo(mymap);

            let directionsTrigger = $('#d0');
            let priceChartTrigger = $('#p0');
            let addPriceTrigger = $('#a0');

            let onClickRoute = async function () {
                await setDestinationAndShowRoute(lng, lat);
            };
            let onClickPriceChart = function () {
                generatePriceChart(currentStation);
            };
            let onClickAddPrices = function () {
                let updatePricesForm = $('#update-prices');
                $(updatePricesForm).removeClass('hide');
                $(updatePricesForm).css('display', '');
                $(updatePricesForm).off();

                let stationDetailsSpan = $('#update-station-details');
                $(stationDetailsSpan).text(currentStation.name + ', ' + currentStation.street);

                $(updatePricesForm).submit(function (event) {
                    event.preventDefault();
                    let url = 'http://localhost:8080/prices/add?id=' + currentStation.id;
                    $.ajax({
                        type: "POST",
                        url: url,
                        data: $(updatePricesForm).serialize(),
                        success: function (response) {
                            console.log(response);
                            if (response === 'success') {
                                alert('Successfully added price');
                                $(updatePricesForm).addClass('hide');
                                $(updatePricesForm).off();
                            } else if (response === 'failure') {
                                alert('Error');
                            } else {
                                alert('Unknown Error');
                            }
                        }
                    });
                });
            };

            $(directionsTrigger).on('click', onClickRoute);
            $(priceChartTrigger).on('click', onClickPriceChart);
            $(addPriceTrigger).one('click', onClickAddPrices);
        }
    }
}

function generatePriceChart(station) {

    let chart = document.getElementById('myChart');
    let ctx = chart.getContext('2d');
    ctx.height = 300;

    let timestamps = [];
    let pb95 = [];
    let pb98 = [];
    let lpg = [];
    let diesel = [];

    if (station.prices.length > 0) {
        for (let i = station.prices.length - 1; i >= 0; i--) {
            if (station.prices.length - i > 15) break;

            timestamps.unshift(station.prices[i].timestamp.substring(0, 16));

            if (station.prices[i].pb95 !== 0.0) {
                pb95.unshift(station.prices[i].pb95);
            } else {
                pb95.unshift(null);
            }
            if (station.prices[i].pb98 !== 0.0) {
                pb98.unshift(station.prices[i].pb98);
            } else {
                pb98.unshift(null);
            }
            if (station.prices[i].lpg !== 0.0) {
                lpg.unshift(station.prices[i].lpg);
            } else {
                lpg.unshift(null);
            }
            if (station.prices[i].diesel !== 0.0) {
                diesel.unshift(station.prices[i].diesel);
            } else {
                diesel.unshift(null);
            }

        }
    }

    if (myChart != null) myChart.destroy();

    myChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: timestamps,
            datasets: [{
                label: 'PB 95',
                data: pb95,
                backgroundColor: 'rgba(255, 99, 132, 0.2)',
                borderColor: 'rgba(255, 99, 132, 1)',
                borderWidth: 1,
                fill: false
            },
                {
                    label: 'PB 98',
                    data: pb98,
                    backgroundColor: 'rgba(54, 162, 235, 0.2)',
                    borderColor: 'rgba(54, 162, 235, 1)',
                    borderWidth: 1,
                    fill: false
                },
                {
                    label: 'LPG',
                    data: lpg,
                    backgroundColor: 'rgba(255, 206, 86, 0.2)',
                    borderColor: 'rgba(255, 206, 86, 1)',
                    borderWidth: 1,
                    fill: false
                },
                {
                    label: 'Diesel',
                    data: diesel,
                    backgroundColor: 'rgba(75, 192, 192, 0.2)',
                    borderColor: 'rgba(75, 192, 192, 1)',
                    borderWidth: 1,
                    fill: false
                }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            spanGaps: true,

            legend: {
                display: true,
                labels: {
                    fontSize: 10
                }
            },
            title: {
                display: true,
                position: 'top',
                text: [station.name + ', ' + station.street + ', ' + station.city, 'Latest prices']
            },
            scales: {
                xAxes: [{
                    ticks: {
                        display: false
                    }
                }],
                yAxes: [{
                    ticks: {
                        beginAtZero: false
                    }
                }]
            },
            layout: {
                padding: {
                    right: 10
                }
            }
        }
    });
    chart.classList.remove('hide');
}

async function updateMarkers(lat, lng, distance, nearbyMarkersAndStations, map) {
    let table = $('#stations-list');
    $(table).addClass('hide');
    clearTable(table);
    let isTableEmpty = true;


    let url = 'http://localhost:8080/stations/get?lat=' + lat + '&lon=' + lng + '&distance=' + distance;
    // global variable
    let nearbyStationsJSON = await fetchJSON(url);
    console.log(nearbyStationsJSON);

    nearbyStationsJSON.forEach((item) => {
        if (item.prices.length > 0) {

            let temp_popup = new mapboxgl.Popup({
                closeButton: true,
                closeOnClick: true
            }).setHTML(
                item.name + '<br><small>' + item.street +
                '</small><br>' +
                '<span id="d0"><b>Directions</b></span> | <span id="p0"><b>Latest Prices</b></span> | <span id="a0"><b>Add Prices</b></span>'
            );

            let temp_marker = new mapboxgl.Marker({
                color: '#0099cc',
                closeOnMove: true,
            })
                .setLngLat([item.lon, item.lat])
                .setPopup(temp_popup)
                .addTo(map);

            nearbyMarkersAndStations.set(temp_marker, item);
            populateTable(table, item);
            isTableEmpty = false;
        }
    });

    if (!isTableEmpty) {
        $(table).removeClass('hide');
        document.querySelectorAll('th').forEach(th => th.addEventListener('click', sortTable));
        $(table).find('tr:nth-child(n+2)').on('click', function () {
            let lng = $(this).find('td:nth-child(6)').text();
            let lat = $(this).find('td:nth-child(7)').text();
            findMarkerAndOpenPopup(nearbyMarkersAndStations, Number(lat), Number(lng));
        });
    }
}

function clearTable(table) {
    $(table).find('tbody > tr:nth-child(n+2)').remove();
}

function clearMarkers(_nearbyMarkersAndStations) {
    for (marker of _nearbyMarkersAndStations.keys()) marker.remove();
    nearbyMarkersAndStations.clear();
}

/* handle dynamic table with prices */
function populateTable(table, data) {

    let pb95 = 0.0;
    let pb98 = 0.0;
    let lpg = 0.0;
    let diesel = 0.0;

    let pb95_set = false;
    let pb98_set = false;
    let lpg_set = false;
    let diesel_set = false;

    for (let i = data.prices.length - 1; i >= 0; i--) {

        if (!pb95_set) {
            if (data.prices[i].pb95 !== 0.0) {
                pb95 = data.prices[i].pb95;
                pb95_set = true;
            }
        }
        if (!pb98_set) {
            if (data.prices[i].pb98 !== 0.0) {
                pb98 = data.prices[i].pb98;
                pb98_set = true;
            }
        }
        if (!lpg_set) {
            if (data.prices[i].lpg !== 0.0) {
                lpg = data.prices[i].lpg;
                lpg_set = true;
            }
        }
        if (!diesel_set) {
            if (data.prices[i].diesel !== 0.0) {
                diesel = data.prices[i].diesel;
                diesel_set = true;
            }
        }
        console.log(i);
        if (pb95_set && pb98_set && lpg_set && diesel_set) break;
    }

    let html = [
        '            <tr>\n' +
        '                <td>\n' +
        '                    <ul>\n' +
        '                        <li>' + data.name + '</li>\n' +
        '                        <li>' + data.street + '</li>\n' +
        '                    </ul>\n' +
        '                </td>\n' +
        '                <td>' + pb95 + '</td>\n' +
        '                <td>' + pb98 + '</td>\n' +
        '                <td>' + lpg + '</td>\n' +
        '                <td>' + diesel + '</td>\n' +
        '                <td style="display: none;">' + data.lon + '</td>\n' +
        '                <td style="display: none;">' + data.lat + '</td>\n' +
        '            </tr>'
    ].join('\n');
    $(table).find('tbody').append(html);
}

mymap.on('load', function () {
    mymap
        .addControl(navigation, 'top-left')
        .addControl(geocoder, 'top-left');
});
mymap.on('dragend', async function () {
    let origin = mymap.getCenter();
    clearMarkers(nearbyMarkersAndStations);
    await updateMarkers(origin.lat, origin.lng, myCircle.getRadius(), nearbyMarkersAndStations, mymap);
    $('#myChart').hide('slow');
    $('#instructions').hide('slow');
    $('#update-prices').hide('slow');

});
mymap.on('move', function () {
    myMarker.setLngLat(mymap.getCenter());
    myCircle.setCenter(mymap.getCenter());
});
mymap.on('click', function (e) {
    e.stopPropagation();
});

myMarker
    .setLngLat(mymap.getCenter())
    .addTo(mymap);

myCircle.addTo(mymap);
myCircle.on('radiuschanged', async function (circleObj) {
    let coords = mymap.getCenter();
    clearMarkers(nearbyMarkersAndStations);
    await updateMarkers(coords.lat, coords.lng, circleObj.getRadius(), nearbyMarkersAndStations, mymap);
    $('#myChart').hide('slow');
    $('#instructions').hide('slow');
    $('#update-prices').hide('slow');
});
