function showThresholds(componentName, m, n) {
    $.ajax({
        type: "POST",
        url: "/control/thresholds",
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

function showAccumulatorsList(componentName, m, n) {
    $.ajax({
        type: "POST",
        url: "/control/accumulatorsList",
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
                showAccumulatorsCharts(componentName, m, n); // function will be called when each checkbox state is changed
            })
        },

        error: function(e){
            window.console && console.warn("Error while loading accumulators for component "+componentName);
        }
    });
}

function showAccumulatorsCharts(componentName, m, n) {
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
        url: "/control/accumulatorsCharts",
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

function fitModalBody(modal) {
    var body, bodypaddings, header, headerheight, height, modalheight, footer, footerheight;
    header = $(".modal-header", modal);
    body = $(".modal-body", modal);
    footer = $(".modal-footer", modal);
    modalheight = parseInt(modal.css("height"));
    headerheight = parseInt(header.css("height")) + parseInt(header.css("padding-top")) + parseInt(header.css("padding-bottom"));
    console.error("HEIGHT: "+header.css("height"));
    footerheight = parseInt(footer.css("height")) + parseInt(footer.css("top"))
    /* bodypaddings = parseInt(body.css("padding-top")) + parseInt(body.css("padding-bottom")); // not applicable when using box-sizing: border-box; */
    height = modalheight - headerheight - footerheight + 1;
    return body.css("max-height", "" + height + "px");
};