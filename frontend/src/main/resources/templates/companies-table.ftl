<#include "_header.ftl">

<#-- @ftlvariable name="data" type="com.investment.com.investment.CompaniesFreemarker" -->

<h3>Companies</h3>

<div class="card">
  <div class="card-content">
  <table class="striped responsive-table">
    <thead class="text-grey">
      <tr>
          <th></th>
          <th class="grey lighten-4">2015</th>
          <th class="grey lighten-4">2016</th>
          <th class="grey lighten-4">2017</th>
          <th class="grey lighten-4">2018</th>
          <th class="grey lighten-4">2019</th>
          <th class="grey lighten-4">Trend</th>
          <th></th>
      </tr>
    </thead>

    <tbody>
    <tr>
        <#list data.items as item>
        <th>Dividends per share</th>
        <#list item.dividendsPerShare as dividendsPerShare>
            <td>
                ${dividendsPerShare.dividendsPerShare}
            </td>
        </#list>
        <td class="light-green lighten-4">Increasing</td>
    </tr>
    <tr>
        <th>Free Cash Flow</th>
        <#list item.freeCashFlow as fcf>
            <td>
                ${fcf.freeCashFlow}
            </td>
        </#list>
        <td class="light-green lighten-4">Increasing</td>
    </tr>
    <tr>
        <th>Net Debt</th>
        <#list item.netDebt as netDebt>
        <td>
            ${netDebt.netDebt}
        </td>
        </#list>
        <td class="yellow lighten-4">Increasing</td>

    </tr>

    <tr>
        <th>Number of Shares</th>
        <#list item.numberOfShares as numberOfShares>
        <td>
            ${numberOfShares.numberOfShares}
        </td>
        </#list>
        <td class="deep-orange lighten-4">Decreasing</td>
    </tr>

    </#list>
    </tbody>
</table>
</body>
</html>
