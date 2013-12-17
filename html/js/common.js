function showAccumulatorsView(componentName, m, n) {
    $.ajax({
        type: "POST",
        url: "/control/accumulatorsView",
        data: {componentName : componentName},
        success: function(response){
            $("#accumulators-view-"+m+n).html(response);
        },
        error: function(e){
            alert("Error: " + e);
        }
    });
}