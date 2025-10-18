// -----------------------------
// CONFIGURATION
// -----------------------------

// Scope options available for each report type
const scopeOptions = {
    countries: ["Global", "Continent", "Region"],
    cities: ["Global", "Continent", "Region", "Country"],
    capitals: ["Global", "Continent", "Region"],
    populations: ["Global", "Continent", "Region", "Country"],
    languages: ["Global", "Continent", "Region", "Country"]
};

// Stable column definitions for each report type
// Ensures consistent table headers regardless of scope
const columnsByReport = {
    countries: ["Code", "Name", "Capital", "Continent", "Population"],
    cities: ["Name", "Country", "District", "Population"],
    capitals: ["Name", "Country", "Continent", "Region", "Population"],
    populations: [
        "Name",
        "Total Population",
        "Living in Cities",
        "Living in Cities (%)",
        "Not Living in Cities",
        "Not Living in Cities (%)"
    ],
    languages: {
        Global:   ["Language", "Number of Speakers", "% of Global Population"],
        Continent:["Language", "Number of Speakers", "% of Continent", "% of Global Population"],
        Region:   ["Language", "Number of Speakers", "% of Region", "% of Global Population"],
        Country:  ["Language", "Number of Speakers", "% of Country", "% of Global Population"]
    }
};

// -----------------------------
// APPLICATION STATE
// -----------------------------

// Tracks current filters, pagination, and data
const state = {
    endpoint: null,          // current report type (countries, cities, etc.)
    scope: "Global",         // current scope (Global, Continent, etc.)
    name: "",                // selected scope name (e.g., "Asia")
    district: "",            // selected district (for cities)
    topN: -1,                // backend Top-N limit (-1 = no limit)
    pageSize: 25,            // frontend rows per page (-1 = show all)
    page: 1,                 // current page number
    data: [],                // dataset returned from backend
    globalPopulation: null   // global population context (for % calculations)
};

// -----------------------------
// DOM ELEMENT REFERENCES
// -----------------------------

const el = {
    breadcrumbs: document.getElementById("breadcrumbs"),
    scopeSelect: document.getElementById("scopeSelect"),
    scopeName: document.getElementById("scopeName"),
    districtLabel: document.getElementById("districtLabel"),
    districtSelect: document.getElementById("districtSelect"),
    topN: document.getElementById("topN"),
    pageSize: document.getElementById("pageSize"),
    apply: document.getElementById("applyFilters"),
    tableContainer: document.getElementById("tableContainer"),
    statusBar: document.getElementById("statusBar"),
    prevPage: document.getElementById("prevPage"),
    nextPage: document.getElementById("nextPage"),
    pageInfo: document.getElementById("pageInfo"),
    jumpPage: document.getElementById("jumpPage"),
    jumpBtn: document.getElementById("jumpBtn"),
    introduction: document.getElementById("introduction"),
    controls: document.getElementById("controls"),
    tableArea: document.getElementById("tableArea"),
};


// -----------------------------
// EVENT HANDLERS
// -----------------------------

// Sidebar menu click → reset filters and load report
document.querySelectorAll(".menu-item").forEach(btn => {
    btn.addEventListener("click", async () => {

        el.introduction.style.display = "none";
        el.controls.style.display = "block";
        el.tableArea.style.display = "block";

        const endpoint = btn.dataset.endpoint;
        setRoute({ endpoint, scope: "Global", name: "", district: "", topN: -1, pageSize: 25, page: 1 });
        updateScopeDropdown(endpoint);
        await updateScopeNameDropdown();
        updateDistrictVisibility();
        fetchAndRender();
    });
});

// Apply button → update state and reload data
el.apply.addEventListener("click", () => {
    setRoute({
        scope: el.scopeSelect.value,
        name: el.scopeName.value || "",
        district: el.districtSelect.value || "",
        topN: parseInt(el.topN.value, 10),
        pageSize: parseInt(el.pageSize.value, 10)
    });
    fetchAndRender();
});

// -----------------------------
// DROPDOWNS
// -----------------------------

// Populate scope dropdown based on report type
function updateScopeDropdown(endpoint) {
    const options = scopeOptions[endpoint] || ["Global"];
    el.scopeSelect.innerHTML = "";
    options.forEach(opt => {
        const option = document.createElement("option");
        option.value = opt;
        option.textContent = opt;
        option.selected = opt === "Global";
        el.scopeSelect.appendChild(option);
    });
}

// When scope changes → refresh scopeName dropdown + district visibility
el.scopeSelect.addEventListener("change", async () => {
    state.scope = el.scopeSelect.value;
    state.name = "";
    await updateScopeNameDropdown();
    updateDistrictVisibility();
});

// Populate scopeName dropdown (continents, regions, countries)
async function updateScopeNameDropdown() {
    const scope = el.scopeSelect.value;
    el.scopeName.innerHTML = "";
    const placeholder = document.createElement("option");
    placeholder.value = "";
    placeholder.textContent = "(All)";
    el.scopeName.appendChild(placeholder);

    if (scope === "Global") return;

    let url;
    if (scope === "Continent") url = "/lookups/continents";
    else if (scope === "Region") url = "/lookups/regions";
    else if (scope === "Country") url = "/lookups/countries";

    if (!url) return;

    try {
        const values = await fetchJSON(url);
        values.forEach(val => {
            const option = document.createElement("option");
            // Countries return {code, name}, others return simple values
            if (val.code && val.name) {
                option.value = val.code;
                option.textContent = val.name;
            } else {
                option.value = val.value || val;
                option.textContent = val.value || val;
            }
            el.scopeName.appendChild(option);
        });
    } catch (e) {
        console.error("Lookup error:", e);
    }
}

// Show/hide district dropdown when scope = Country/District
function updateDistrictVisibility() {
    const isCities = state.endpoint === "cities" || "population";
    const scope = el.scopeSelect.value;
    const showDistrict = isCities && (scope === "Country");
    el.districtLabel.style.display = showDistrict ? "inline-block" : "none";
    if (showDistrict) loadDistrictsForSelectedCountry();
}

// Populate district dropdown for selected country
async function loadDistrictsForSelectedCountry() {
    el.districtSelect.innerHTML = "";
    const placeholder = document.createElement("option");
    placeholder.value = "";
    placeholder.textContent = "(All districts)";
    el.districtSelect.appendChild(placeholder);

    const country = el.scopeSelect.value === "Country" ? el.scopeName.value : "";
    if (!country) return;

    try {
        const districts = await fetchJSON(`/lookups/districts?country=${encodeURIComponent(country)}`);
        districts.forEach(d => {
            const option = document.createElement("option");
            option.value = d.value || d;
            option.textContent = d.value || d;
            el.districtSelect.appendChild(option);
        });
    } catch (e) {
        console.error("District lookup error:", e);
    }
}

// When country changes → refresh districts
el.scopeName.addEventListener("change", () => {
    if (state.endpoint === "cities" && el.scopeSelect.value === "Country") {
        loadDistrictsForSelectedCountry();
    }
});

// -----------------------------
// ROUTING + API
// -----------------------------

// Update state and breadcrumbs
function setRoute(partial) {
    Object.assign(state, partial);
    state.page = 1;
    el.breadcrumbs.textContent = buildBreadcrumbs();
}

// Build breadcrumb trail
function buildBreadcrumbs() {
    const parts = ["Home"];
    if (state.endpoint) parts.push(titleCase(state.endpoint));
    if (state.scope) parts.push(state.scope);
    if (state.name) parts.push(state.name);
    if (state.endpoint === "cities" && state.district) parts.push(state.district);
    return parts.join(" › ");
}

// Construct API URL with filters
function apiUrl() {
    const params = new URLSearchParams();
    if (state.scope && state.scope !== "Global") params.set("scope", state.scope.toLowerCase());
    if (state.name) params.set("name", state.name);
    if (state.endpoint === "cities" && state.district) params.set("district", state.district);
    if (state.topN !== -1) params.set("limit", state.topN); // Top-N only
    return `/reports/${state.endpoint}?${params.toString()}`;
}

// -----------------------------
// DATA FETCH + RENDER
// -----------------------------

// Fetch data from backend and render table
async function fetchAndRender() {
    if (!state.endpoint) return;
    try {
        el.statusBar.textContent = "Loading…";
        const res = await fetch(apiUrl());
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        const response = await res.json();

        state.globalPopulation = response.globalPopulation || response[0].globalPopulation || null;
        state.data = response.data || response;

        renderTable();
        el.statusBar.textContent = `Loaded ${state.data.length} rows`;
    } catch (err) {
        el.tableContainer.innerHTML = `<p>Error: ${escapeHTML(err.message)}</p>`;
        el.statusBar.textContent = "Error";
    }
}

// Render table with stable columns + pagination
// Render table with stable columns + pagination
function renderTable() {
    let html = "";

// Context banner above the table
    if (state.endpoint === "languages") {
        let scopeLabel = "";
        let percentOfGlobal = null;

        switch (state.scope) {
            case "Global":
                scopeLabel = "Global Population";
                // For global, it's always 100%
                percentOfGlobal = "100.00";
                break;
            case "Continent":
                scopeLabel = `Continent: ${state.name || "(All)"}`;
                // Use the first row’s % of global as context
                if (state.data.length > 0) {
                    percentOfGlobal = state.data[0].percentOfGlobalPopulation
                        || state.data[0].percentofglobalpopulation;
                }
                break;
            case "Region":
                scopeLabel = `Region: ${state.name || "(All)"}`;
                if (state.data.length > 0) {
                    percentOfGlobal = state.data[0].percentOfGlobalPopulation
                        || state.data[0].percentofglobalpopulation;
                }
                break;
            case "Country":
                scopeLabel = `Country: ${state.name || "(All)"}`;
                if (state.data.length > 0) {
                    percentOfGlobal = state.data[0].percentOfGlobalPopulation
                        || state.data[0].percentofglobalpopulation;
                }
                break;
        }

        html += `<p><strong>${scopeLabel}</strong></p>`;
    } else if (state.globalPopulation) {
        // For population reports, show global population if available
        html += `<p><strong>Total Global Population:</strong> ${numberFmt(state.globalPopulation)}</p>`;
    }

    // Handle empty dataset
    if (!Array.isArray(state.data) || state.data.length === 0) {
        el.tableContainer.innerHTML = html + "<p>No data available</p>";
        updatePagination();
        return;
    }

    // Choose headers: scope‑aware for languages, flat for everything else
    const baseCols = columnsByReport[state.endpoint];
    const headers = (state.endpoint === "languages")
        ? baseCols[state.scope] || baseCols.Global
        : baseCols;

    const pageRows = getPageRows();

    // Build table
    html += "<table><thead><tr>";
    headers.forEach(h => html += `<th>${escapeHTML(h.toUpperCase())}</th>`);
    html += "</tr></thead><tbody>";

    pageRows.forEach(row => {
        html += "<tr>";
        headers.forEach(h => {
            const val = deriveCellValue(state.endpoint, h, row, state.globalPopulation);
            html += `<td>${escapeHTML(formatCell(val))}</td>`;
        });
        html += "</tr>";
    });

    html += "</tbody></table>";
    el.tableContainer.innerHTML = html;
    updatePagination();
}

function getPageRows() {
    if (state.pageSize === -1) return state.data;
    const start = (state.page - 1) * state.pageSize;
    return state.data.slice(start, start + state.pageSize);
}

function updatePagination() {
    if (state.pageSize === -1) {
        el.prevPage.disabled = true;
        el.nextPage.disabled = true;
        el.pageInfo.textContent = `Showing all ${state.data.length} records`;
        return;
    }

    const totalPages = Math.max(1, Math.ceil(state.data.length / state.pageSize));
    el.prevPage.disabled = state.page <= 1;
    el.nextPage.disabled = state.page >= totalPages;
    el.pageInfo.textContent = `Page ${state.page} / ${totalPages}`;
}

el.prevPage.addEventListener("click", () => {
    if (state.page > 1) {
        state.page--;
        renderTable();
    }
});

el.nextPage.addEventListener("click", () => {
    const totalPages = Math.max(1, Math.ceil(state.data.length / state.pageSize));
    if (state.page < totalPages) {
        state.page++;
        renderTable();
    }
});

el.jumpBtn.addEventListener("click", () => {
    if (state.pageSize === -1) return;
    const totalPages = Math.max(1, Math.ceil(state.data.length / state.pageSize));
    const target = parseInt(el.jumpPage.value, 10);
    if (!isNaN(target) && target >= 1 && target <= totalPages) {
        state.page = target;
        renderTable();
    }
});

// Helpers
async function fetchJSON(url) {
    const res = await fetch(url);
    if (!res.ok) throw new Error(`HTTP ${res.status}`);
    return await res.json();
}

function deriveCellValue(endpoint, header, row, global) {
    const m = lowerKeys(row);

    if (endpoint === "populations") {
        const total = num(m.totalpopulation);
        const city = num(m.citypopulation);
        const nonCity = num(m.noncitypopulation);

        switch (header) {
            case "Label": return m.label;
            case "Total Population": return total;
            case "Living in Cities": return city;
            case "Living in Cities (%)": return total > 0 ? ((city * 100) / total).toFixed(2) : "0.00";
            case "Not Living in Cities": return nonCity;
            case "Not Living in Cities (%)": return total > 0 ? ((nonCity * 100) / total).toFixed(2) : "0.00";
        }
    }

    if (endpoint === "languages") {
        switch (header) {
            case "Language": return m.language || m.name;
            case "Number of Speakers": return m.numberofspeakers || m.speakers;
            case "% of Global Population": return m.percentofglobalpopulation || "0.00";
            case "% of Continent": return m.percentofcontinentpopulation || "0.00";
            case "% of Region": return m.percentofregionpopulation || "0.00";
            case "% of Country": return m.percentofcountrypopulation || "0.00";
        }
    }


    return m[header.toLowerCase()] ?? row[header] ?? "";
}

function lowerKeys(obj) {
    const out = {};
    Object.keys(obj || {}).forEach(k => (out[k.toLowerCase()] = obj[k]));
    return out;
}

function num(n) {
    const v = Number(n);
    return Number.isFinite(v) ? v : 0;
}

function formatCell(value) {
    if (value == null) return "";
    if (typeof value === "number") return numberFmt(value);
    return String(value);
}

function numberFmt(n) {
    try { return Number(n).toLocaleString(); } catch { return String(n); }
}

function escapeHTML(value) {
    return String(value ?? "")
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;");
}

function titleCase(s) {
    if (!s) return "";
    return s.charAt(0).toUpperCase() + s.slice(1);
}
