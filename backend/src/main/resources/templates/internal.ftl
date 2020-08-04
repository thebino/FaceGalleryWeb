<#include "_header.ftl">

<h3>Internal</h3>

<div class="row">
    <#list images as image>
    <div class="col s12 m4 l2">
        <div class="card">
            <div class="card-image">
                <img src="https://via.placeholder.com/150">
            </div>
            <div class="card-content">
                <p>${image.faces?size} Faces</p>
                <p>${image.filePath}</p>
            </div>
        </div>
    </div>
    </#list>
</div>

<div class="card">
    <div class="card-content">
        <div>User: ${user}</div>
        <div>TensorFlow Version: ${version}</div>

    </div>
</div>

<#include "_footer.ftl">
