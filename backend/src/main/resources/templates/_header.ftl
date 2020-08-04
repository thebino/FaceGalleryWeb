<!DOCTYPE html>
<html>
  <head>
    <title>FaceGallery</title>

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>

    <!--Let browser know website is optimized for mobile-->
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>

    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <link href="static/materialize.min.css" type="text/css" rel="stylesheet" media="screen,projection"/>

    <script src="https://code.jquery.com/jquery-2.1.1.min.js"></script>
  </head>
<body>

<nav class="light-blue lighten-1" role="navigation">
  <div class="nav-wrapper container">
    <a href="/" class="brand-logo"><i class="material-icons">camera</i>FaceGallery</a>

    <a href="#" data-target="mobile-demo" class="sidenav-trigger"><i class="material-icons">menu</i></a>
    <ul id="nav-mobile" class="right hide-on-med-and-down">
        <li><a href="/">Overview</a></li>
        <li><a href="/faces">Faces</a></li>
        <#if user??>
        <li><a href="/logout" >Logout</a></li>
        </#if>
    </ul>

  </div>
</nav>

<ul class="sidenav" id="mobile-demo">
    <li><a href="/">Overview</a></li>
    <li><a href="/faces">Faces</a></li>
    <#if user??>
    <li><a href="/logout">Logout</a></li>
    </#if>
</ul>

<div class="container">
