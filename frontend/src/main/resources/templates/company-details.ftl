<#include "_header.ftl">

<h3>Company Details</h3>
<p>${company.companyName}</p>

<div class="card">
  <div class="card-content">
  <table class="striped responsive-table">
    <thead class="text-grey">
      <tr>
        <th></th>
        <#list company.dividendsPerShare?keys as key>
            <th class="center-align">${key?string["0"]}</th>
        </#list>
        <th>
      </tr>
    </thead>

    <tbody>
    <tr>
        <th>Dividends per share</th>
        <#list company.dividendsPerShare as key, value>
            <td class="center-align">${value}</td>
        <#else>
            <th></th>
        </#list>
        <td class="light-green lighten-4">Increasing</td>
    </tr>

    <tr>
        <th>Free Cash Flow</th>
        <#list company.freeCashFlow as key, value>
            <td class="center-align">${value}</td>
        <#else>
            <th></th>
        </#list>
        <td class="light-green lighten-4">Increasing</td>
    </tr>

    <tr>
        <th>Net Debt</th>
        <#list company.netDebt as key, value>
            <td class="center-align">${value}</td>
        <#else>
            <th></th>
        </#list>
        <td class="light-green lighten-4">Increasing</td>
    </tr>

    <tr>
        <th>Number of Shares</th>
        <#list company.netDebt as key, value>
            <td class="center-align">${value}</td>
        <#else>
            <th></th>
        </#list>
        <td class="light-green lighten-4">Increasing</td>
    </tr>

    </tbody>
  </div>
</div>
</table>
</body>
</html>
