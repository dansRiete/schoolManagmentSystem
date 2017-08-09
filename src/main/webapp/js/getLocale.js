/**
 * Created by Aleks on 09.08.2017.
 */
function getMyLocale() {
    let link = $(location).attr("protocol") + "//" + $(location).attr("host") + "/locale";
    $.ajax({url: link, type: "GET", async: false})
        .fail(function () {
            console.log("Getting locale failed")
        })
        .done(function (msg) {
            let locale = msg['locale_language'];
        });

}