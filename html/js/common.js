function showAccumulatorsList(componentName, m, n) {
    $.ajax({
        type: "POST",
        url: "/control/accumulatorsList",
        data: {componentName : componentName, id : m+n},
        beforeSend: function(){
            $("#accumulators-list-"+m+n).empty();
            $("#accumulators-charts-"+m+n).empty();
            $("#loading-indicator").appendTo("#accumulators-view-"+m+n);
            $("#loading-indicator").show();
        },
        complete: function(){
            $("#loading-indicator").appendTo("body");
            $("#loading-indicator").hide();
        },
        success: function(response){
            $("#accumulators-list-"+m+n).html(response);
        },
        error: function(e){
            alert("Error: " + e);
        }
    });
}

function showAccumulatorsCharts(componentName, m, n) {
    $.ajax({
        type: "POST",
        url: "/control/accumulatorsCharts",
        data: {componentName : componentName, id : m+n},
        beforeSend: function(){
            $("#accumulators-charts-"+m+n).empty();
            $("#loading-indicator").appendTo("#accumulators-view-"+m+n);
            $("#loading-indicator").show();
        },
        complete: function(){
            $("#loading-indicator").appendTo("body");
            $("#loading-indicator").hide();
        },
        success: function(response){
            $("#accumulators-charts-"+m+n).html(response);
        },
        error: function(e){
            alert("Error: " + e);
        }
    });
}

function fitModalBody(modal) {
    var body, bodypaddings, header, headerheight, height, modalheight;
    header = $(".modal-header", modal);
    body = $(".modal-body", modal);
    modalheight = parseInt(modal.css("height"));
    headerheight = parseInt(header.css("height")) + parseInt(header.css("padding-top")) + parseInt(header.css("padding-bottom"));
    bodypaddings = parseInt(body.css("padding-top")) + parseInt(body.css("padding-bottom"));
    height = modalheight - headerheight - bodypaddings - 5;
    return body.css("max-height", "" + height + "px");
};