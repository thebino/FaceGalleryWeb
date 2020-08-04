<#include "_header.ftl">

<div class="row">
    <#list images as image>
    <div class="col s12 m4 l2">
        <div class="card">
            <div class="card-image">
                <img src="images/${image.imagePath}">
            </div>
            <div class="card-content">
            <p>${image.faces?size} Faces</p>

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
