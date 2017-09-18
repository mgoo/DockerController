/// <reference path="definitions/jquery/jquery.d.ts" />
/// <reference path="DockerController.ts" />

/**
 * Created by mgoo on 11/02/17.
 */
class TableFixer{
    constructor(){

    }

    static makeContainerButtons(DockerController: DockerControllerWrapper): void{
        let startButton = $('<span></span>');
        startButton.click(function (event) {
            let containerID = $(event.target).closest('td').clone().children().remove().end().text();
            DockerController.startContainer(containerID);
            DockerController.getRunning();
        });
        startButton.html('Start');
        startButton.addClass('waves-effect waves-light btn green');

        let stopButton = $('<span></span>');
        stopButton.click(function (event) {
            let containerID = $(event.target).closest('td').clone().children().remove().end().text();
            DockerController.stopContainer(containerID);
            DockerController.getRunning();
        });
        stopButton.html('Stop');
        stopButton.addClass('waves-effect waves-light btn red accent-4');

        $('table tr:not(:nth-child(1)) td:nth-child(1)').append('<br>')
        $('table tr:not(:nth-child(1)) td:nth-child(1)').append(startButton);
        $('table tr:not(:nth-child(1)) td:nth-child(1)').append(stopButton);
    }
}