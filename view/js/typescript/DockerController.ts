/// <reference path="definitions/jquery/jquery.d.ts" />
/// <reference path="TableFixer.ts" />

class DockerControllerWrapper{
    constructor(){

    }
    exit(){
        window.close();
    }
    getRunning(): string{
        return this.loadPage('/DockerController/getRunning', null);
    }
    getAllContainers(): string{
        return this.loadPage('/DockerController/getAllContainers', null);
    }
    getVersion(): string{
        return this.loadPage('/DockerController/getVersion', null);
    }
    getInfo(): string{
        return this.loadPage('/DockerController/getInfo', null);
    }
    getQuickVersion(): string{
        return this.loadPage('/DockerController/getQuickVersion', null);
    }
    getImages(): string{
        return this.loadPage('/DockerController/getImages', null);
    }
    startContainer(name: string): string{
        return this.loadPage('/DockerController/startContainer', {a: name});
    }
    stopContainer(name: string): string{
        return this.loadPage('/DockerController/stopContainer', {a: name});
    }
    private loadPage(page: string, data: Object): string{
        let result = '';
        $.ajax({
            async: false,
            url: window.location.origin + page,
            data: data,
            success: function(response){
                result = response;
            }
        });
        return result;
    }
}