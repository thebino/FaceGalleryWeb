<#include "_header.ftl">

<div class="section">
<div class="row">
  <div class="col s12">

    <h3>FaceGallery</h3>
    <p>Welcome</p>

    <form action="/" method="POST" enctype="application/x-www-form-urlencoded">
        <div class="input-field col s12">
        <input type="text" placeholder="Enter Username" name="username" class="validate">
        <label for="username"><b>Username</b></label>
        </div>

        <div class="input-field col s12">
        <input type="password" placeholder="Enter Password" name="password" class="validate">
        <label for="password"><b>Password</b></label>
        </div>

        <div class="input-field col s12">
        <button type="submit">Login</button>
        </div>

    </form>
  </div>
</div>
</div>

<#include "_footer.ftl">
