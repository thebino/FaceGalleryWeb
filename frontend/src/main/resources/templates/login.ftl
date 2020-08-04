<#include "_header.ftl">

<div class="row" style="margin-top: 50px;">
  <div class="col s6 offset-s3">
    <form action="/" method="POST" enctype="application/x-www-form-urlencoded">
    <div class="card darken-1">
      <div class="card-content teal-text darken-4">
        <span class="card-title center-align">Anmelden</span>
        <div class="input-field col s12">
          <input type="text" placeholder="Enter Username" name="username" class="validate">
          <label for="username"><b>Username</b></label>
        </div>

        <div class="input-field col s12">
          <input type="password" placeholder="Enter Password" name="password" class="validate">
          <label for="password"><b>Password</b></label>
        </div>

        <a href="/usernamerecovery" class="teal-text darken-4" style="clear: both;">E-Mail-Adresse vergessen?</span>
      </div>
      <div class="card-action">
        <div class="right-align">
          <button type="submit" style="border: none;text-decoration: none;background: none;">Weiter</button>
        </div>
      </div>
      </form>
    </div>
  </div>
</div>

<#include "_footer.ftl">
