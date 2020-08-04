<#include "_header.ftl">

<h3>Internal</h3>
<div class="card">
    <div class="card-content">
        User: ${username}
        TensorFlow Version: ${version}
        <input type="text" id="filterInput" onkeyup="filterTable()" placeholder="Search for names..">

        <table class="striped responsive-table" id="dataTable">
            <thead class="text-grey">
            <tr>
                <th>Company</th>
                <th>Isin</th>
                <th>Ticker</th>
            </tr>
            </thead>

            <tbody>

            <#list companies as company>
            <tr onclick="document.location = '?isin=${company.isin}';" style="cursor: pointer;">
                <td>${company.companyName}</td>
                <td>${company.isin}</td>
                <td>${company.ticker}</td>
            </tr>
            </#list>

            </tbody>
        </table>
    </div>
</div>

<script>
function filterTable() {
  var input, filter, table, tr, companyName, companyIsin, companySymbol, i, txtValue, symbolValue;
  input = document.getElementById("filterInput");
  filter = input.value.toUpperCase();
  table = document.getElementById("dataTable");
  tr = table.getElementsByTagName("tr");

  // Loop through all table rows, and hide those who don't match the search query
  for (i = 0; i < tr.length; i++) {
    companyName = tr[i].getElementsByTagName("td")[0];
    companyIsin = tr[i].getElementsByTagName("td")[1];
    companySymbol = tr[i].getElementsByTagName("td")[2];

    if (companyName && companyIsin && companySymbol) {
      txtValue = companyName.textContent || companyName.innerText;
      isinValue = companyIsin.textContent || companyIsin.innerText;
      symbolValue = companySymbol.textContent || companySymbol.innerText;

      if (txtValue.toUpperCase().indexOf(filter) > -1 || isinValue.toUpperCase().indexOf(filter) > -1 || symbolValue.toUpperCase().indexOf(filter) > -1) {
        tr[i].style.display = "";
      } else {
        tr[i].style.display = "none";
      }
    }
  }
}
</script>


<#include "_footer.ftl">
