/* get JSON from URL */
async function fetchJSON(url, init) {
    const response = await fetch(url, init);
    if (!response.ok) {
        throw new Error("HTTP error " + response.status);
    }
    return response.json();
}

/* sort tables */
const getCellValue = (tr, idx) => tr.children[idx].innerText || tr.children[idx].textContent;

const comparer = (idx, asc) => (a, b) => ((v1, v2) =>
        v1 !== '' && v2 !== '' && !isNaN(v1) && !isNaN(v2) ? v1 - v2 : v1.toString().localeCompare(v2)
)(getCellValue(asc ? a : b, idx), getCellValue(asc ? b : a, idx));

function sortTable(event) {
        const table = this.closest('table');
        const tbody = table.querySelector('tbody');
        Array.from(tbody.querySelectorAll('tr:nth-child(n+2)'))
            .sort(comparer(Array.from(this.parentNode.children).indexOf(this), this.asc = !this.asc))
            .forEach(tr => tbody.appendChild(tr));
}

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

function isNotNullNorUndefined(o) {
    return (typeof (o) !== 'undefined' && o !== null);
}