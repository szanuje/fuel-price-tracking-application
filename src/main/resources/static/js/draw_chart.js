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
            datasets: [
                {
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
                    fontSize: 9
                }
            },
            title: {
                display: true,
                position: 'top',
                text: [station.name + ', ' + station.street +
                ', ' + station.city, 'Latest prices']
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