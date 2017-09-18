/// <reference path="definitions/jquery/jquery.d.ts" />
/// <reference path="TableFixer.ts" />
var DockerControllerWrapper = (function () {
    function DockerControllerWrapper() {
    }
    DockerControllerWrapper.prototype.exit = function () {
        window.close();
    };
    DockerControllerWrapper.prototype.getRunning = function () {
        return this.loadPage('/DockerController/getRunning', null);
    };
    DockerControllerWrapper.prototype.getAllContainers = function () {
        return this.loadPage('/DockerController/getAllContainers', null);
    };
    DockerControllerWrapper.prototype.getVersion = function () {
        return this.loadPage('/DockerController/getVersion', null);
    };
    DockerControllerWrapper.prototype.getInfo = function () {
        return this.loadPage('/DockerController/getInfo', null);
    };
    DockerControllerWrapper.prototype.getQuickVersion = function () {
        return this.loadPage('/DockerController/getQuickVersion', null);
    };
    DockerControllerWrapper.prototype.getImages = function () {
        return this.loadPage('/DockerController/getImages', null);
    };
    DockerControllerWrapper.prototype.startContainer = function (name) {
        return this.loadPage('/DockerController/startContainer', { a: name });
    };
    DockerControllerWrapper.prototype.stopContainer = function (name) {
        return this.loadPage('/DockerController/stopContainer', { a: name });
    };
    DockerControllerWrapper.prototype.loadPage = function (page, data) {
        var result = '';
        $.ajax({
            async: false,
            url: window.location.origin + page,
            data: data,
            success: function (response) {
                result = response;
            }
        });
        return result;
    };
    return DockerControllerWrapper;
}());
