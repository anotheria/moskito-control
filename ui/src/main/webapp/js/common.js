function showThresholds(appContext, componentName, m, n) {
    $.ajax({
        type: "POST",
        url: appContext+"/control/thresholds",
        data: {componentName : componentName},

        beforeSend: function(){
            $("#thresholds-view-"+m+n).empty(); // cleaning view content before its loading/reloading
            $("#thresholds-view-"+m+n).hide();
            $(".loading", "#thresholds-tab-"+m+n).show();
        },

        complete: function(){
            $("#thresholds-view-"+m+n).show();
            $(".loading", "#thresholds-tab-"+m+n).hide();
        },

        success: function(response){
            $("#thresholds-view-"+m+n).html(response); // we've got thresholds
        },

        error: function(e){
            window.console && console.warn("Error while loading thresholds for component "+componentName);
        }
    });
}

function showAccumulatorsList(appContext, componentName, m, n) {
    $.ajax({
        type: "POST",
        url: appContext+"/control/accumulatorsList",
        data: {componentName : componentName},

        beforeSend: function(){
            $("#accumulators-view-"+m+n).empty(); // cleaning view content before its loading/reloading
            $("#accumulators-view-"+m+n).hide();
            $(".loading", "#accumulators-tab-"+m+n).show();
        },

        complete: function(){
            $("#accumulators-view-"+m+n).show();
            $(".loading", "#accumulators-tab-"+m+n).hide();
        },

        success: function(response){
            $("#accumulators-view-"+m+n).html(response); // we've got checkboxes & accumulators names list
            accumulatorsList = $(".accumulators-list", "#accumulators-view-"+m+n); // we are interested in accumulators-list class within concrete view id
            $("input:checkbox", accumulatorsList).change( function () {
                showAccumulatorsCharts(appContext, componentName, m, n); // function will be called when each checkbox state is changed
            })
        },

        error: function(e){
            window.console && console.warn("Error while loading accumulators for component "+componentName);
        }
    });
}

function showAccumulatorsCharts(appContext, componentName, m, n) {
    var accumulators = [];

    $("input:checkbox:checked", accumulatorsList).each( function () {
        accumulators.push($(this).attr('name')); // collecting checked accumulators names to load appropriate charts then
    });

    accumulatorsCharts = $(".accumulators-charts", "#accumulators-view-"+m+n);

    /* if there are no checked elements */
    if (accumulators.length == 0 && accumulatorsCharts) {
        $(accumulatorsCharts).remove(); // remove charts block and that's all until the next checkbox checking
        return;
    }

    /* if it's not the first tab loading before full page refresh */
    if (accumulatorsCharts) {
        $(accumulatorsCharts).remove(); // than remove old charts block before loading the new one
    }

    $.ajax({
        type: "POST",
        url: appContext+"/control/accumulatorsCharts",
        data: {componentName : componentName, accumulators : accumulators},

        beforeSend: function(){
            $("#accumulators-view-"+m+n).hide();
            $(".loading", "#accumulators-tab-"+m+n).show();
        },

        complete: function(){
            $("#accumulators-view-"+m+n).show();
            $(".loading", "#accumulators-tab-"+m+n).hide();
        },

        success: function(response){
            $(response).prependTo("#accumulators-view-"+m+n); // placing received charts before accumulators names list

            /*
             Response contains JS text inside script tag that executes when response is being prepend,
             even thought script tag is not present page source after response prepending.

             Another solution would be to extract script tag from response and append it without JQuery:

                 $(response).filter('script').each( function () {
                     var script = document.createElement('script');
                     script.type = "text/javascript";
                     script.text = this.text;
                     // and append this script to charts div
                 });
             */
        },

        error: function(e){
            window.console && console.warn("Error while loading charts for component "+componentName);
        }
    });
}

function showConnectorInformation(appContext, componentName, m, n) {
    $.ajax({
        type: "POST",
        url: appContext+"/control/connectorInformation",
        data: { componentName : componentName },

        beforeSend: function(){
            $("#info-view-"+m+n).empty();
            $("#info-view-"+m+n).hide();
            $(".loading", "#info-tab-"+m+n).show();
        },

        complete: function(){
            $("#info-view-"+m+n).show();
            $(".loading", "#info-tab-"+m+n).hide();
        },

        success: function(response){
            $("#info-view-"+m+n).html(response);
        },

        error: function(e){
            window.console && console.warn("Error while loading connector information for component " + componentName);
        }
    });
}

function showComponentInformation(appContext, componentName, m, n) {
    $.ajax({
        type: "POST",
        url: appContext+"/control/componentInformation",
        data: { componentName : componentName },

        beforeSend: function(){
            $("#component-view-"+m+n).empty();
            $("#component-view-"+m+n).hide();
            $(".loading", "#component-tab-"+m+n).show();
        },

        complete: function(){
            $("#component-view-"+m+n).show();
            $(".loading", "#component-tab-"+m+n).hide();
        },

        success: function(response){
            $("#component-view-"+m+n).html(response);
        },

        error: function(e){
            window.console && console.warn("Error while loading component information for component " + componentName);
        }
    });
}

function showComponentActionInformation(appContext, componentName, m, n) {
    $.ajax({
        type: "POST",
        url: appContext + "/control/componentActionInformation",
        data: {componentName: componentName},

        beforeSend: function () {
            $("#component-action-view-" + m + n).empty();
            $("#component-action-view-" + m + n).hide();
            $(".loading", "#component-action-tab-" + m + n).show();
        },

        complete: function () {
            $("#component-action-view-" + m + n).show();
            $(".loading", "#component-action-tab-" + m + n).hide();
        },

        success: function (response) {
            $("#component-action-view-" + m + n).html(response);
            $('.execute-command').attr("current-index", "" + m + n);
        },

        error: function (e) {
            window.console && console.warn("Error while loading component action information for component " + componentName);
        }
    });
}

function executeComponentActionCommand(appContext, componentName, name, current) {
    let index = $(current).attr("current-index");
    $.ajax({
        type: "POST",
        url: appContext + "/control/executeComponentActionCommand",
        data: {componentName: componentName, name: name},
        beforeSend: function () {
            $('.command-result').remove();
            $("#component-action-view-" + index).hide();
            $(".loading", "#component-action-tab-" + index).show();
        },

        complete: function () {
            $("#component-action-view-" + index).show();
            $(".loading", "#component-action-tab-" + index).hide();
        },
        success: function (response) {
            $(response).prependTo("#component-action-view-" + index);
        },

        error: function (e) {
            window.console && console.warn("Error while loading component action information for component " + command);
        }
    });
}

function showHistory(appContext, componentName, m, n) {
    $.ajax({
        type: "POST",
        url: appContext + "/control/componentHistory",
        data: { componentName: componentName },

        beforeSend: function () {
            $("#history-view-" + m + n).empty();
            $("#history-view-" + m + n).hide();
            $(".loading", "#history-tab-" + m + n).show();
        },

        complete: function () {
            $("#history-view-" + m + n).show();
            $(".loading", "#history-tab-" + m + n).hide();
        },

        success: function (response) {
            $("#history-view-" + m + n).html(response);
        },

        error: function (e) {
            window.console && console.warn("Error while retrieving history for component " + componentName);
        }
    });
}

function showConfig(appContext, componentName, m, n) {
    $.ajax({
        type: "POST",
        url: appContext + "/control/componentConfig",
        data: { componentName: componentName },

        beforeSend: function () {
            let $configView = $("#config-view-" + m + n);
            $configView.empty();
            $configView.hide();
            $(".loading", "#config-tab-" + m + n).show();
        },

        complete: function () {
            $("#config-view-" + m + n).show();
            $(".loading", "#config-tab-" + m + n).hide();
        },

        success: function (response) {
            $("#config-view-" + m + n).html(response);
        },

        error: function (e) {
            window.console && console.warn("Error while retrieving config for component " + componentName);
        }
    });
}

function applyConnectorConfiguration(appContext, applicationName, componentName, m, n) {
    $.ajax({
        type: "GET",
        url: appContext + "/rest/connectors/configuration/" + componentName,

        beforeSend: function() {
            $("#thresholds-tab-toggle-"+m+n).show();
            $("#accumulators-tab-toggle-"+m+n).show();
            $("#info-tab-toggle-"+m+n).show();
        },

        success: function( response ) {
            var connectorConfig = response['connectorConfiguration'];

            if (!connectorConfig) {
                return;
            }

            if (!connectorConfig['supportsThresholds']) {
                $("#thresholds-tab-"+m+n).hide();
                $("#thresholds-tab-toggle-"+m+n).hide();
            }

            if (!connectorConfig['supportsAccumulators']) {
                $("#accumulators-tab-"+m+n).hide();
                $("#accumulators-tab-toggle-"+m+n).hide();
            }

            if (!connectorConfig['supportsInfo']) {
                $("#info-tab-"+m+n).hide();
                $("#info-tab-toggle-"+m+n).hide();
            }
        },

        error: function(e) {
            window.console && console.warn("Error while loading connector information for component " + componentName);
        }
    });
}

function fitModalBody(modal) {
    var body, bodypaddings, header, headerheight, height, modalheight, footer, footerheight;
    header = $(".modal-header", modal);
    body = $(".modal-body", modal);
    footer = $(".modal-footer", modal);
    modalheight = parseInt(modal.css("height"));
    headerheight = parseInt(header.css("height")) + parseInt(header.css("padding-top")) + parseInt(header.css("padding-bottom"));
    footerheight = parseInt(footer.css("height")) + parseInt(footer.css("top"))
    /* bodypaddings = parseInt(body.css("padding-top")) + parseInt(body.css("padding-bottom")); // not applicable when using box-sizing: border-box; */
    height = modalheight - headerheight - footerheight + 1;
    return body.css("max-height", "" + height + "px");
};
