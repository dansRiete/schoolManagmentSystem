/**
 * Created by Aleks on 09.08.2017.
 */
function getMyLocale() {
    console.log('getMyLocale() called');
    let link = $(location).attr("protocol") + "//" + $(location).attr("host") + "/locale";
    let locale = 'en';
    $.ajax({url: link, type: "GET"})
        .fail(function () {
            console.log("Getting locale failed")
        })
        .done(function (msg) {
            console.log('locale_language ' + msg['locale_language']);
            return msg['locale_language'];
        });
}