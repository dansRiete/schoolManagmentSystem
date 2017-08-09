/**
 * Created by Aleks on 01.08.2017.
 */
$(document).ready(
    $('#modalAddGrade').on('hidden.bs.modal', function () {
        window.location.reload(true);
    })
);

function showAddGradeModal(){
    $("#statusMessage").html("");
    $('#modalAddGrade').modal({show: true});
}

function submitAverage(){
    let form = $("#filterForm");
    let link = $(location).attr("protocol") + "//" + $(location).attr("host") + "/average";
    $.ajax({url: link, type: "POST", data: form.serialize()})
        .fail(function () {
            console.log("Sending form failed")
        })
        .done(function (msg) {
            if(msg["avg"] !== null && msg["avg"] !== undefined && msg["avg"] !== ''){
                $("#modal-body-text").html(msg["avg"]);
                $("#modal-title-text").html(
                    'Average mark on:' +
                    (msg["subjectTitle"] === '' ? '\r\nSubject: all subjects' : ("\r\nSubject - " + msg["subjectTitle"] + ';')) +
                    (msg["selectedDate"] === '' ? '\r\nDate: all dates' :   ("\r\nDate - " + msg["selectedDate"] + ';'))
                );
            }else {
                $("#modal-body-text").html('There are no grades on requested conditions');
                $("#modal-title-text").html(
                    'Average mark on:' +
                    (msg["subjectTitle"] === '' ? '\r\nSubject: all subjects' : ("\r\nSubject - " + msg["subjectTitle"] + ';')) +
                    (msg["selectedDate"] === '' ? '\r\nDate: all dates' :   ("\r\nDate - " + msg["selectedDate"] + ';'))
                );
            }
            $('#myModal').modal({show: true});
        });
}

function resetFilter(){
    let form = $("#filterForm");
    form[0].reset();
    $("#subjectsSelect").val(null);
    $("#selectedDate").val(null);
    form[0].submit();
}

function addGrade(form) {
    let link = $(location).attr("protocol") + "//" + $(location).attr("host") + "/locale";
    $.ajax({url: link, type: "GET"})
        .fail(function () {
            addGradeToServer(form, 'en');
        })
        .done(function (msg) {
            let locale = msg['locale_language'];
            addGradeToServer(form, locale === undefined || locale === null ? 'en' : locale);
        });
}

function addGradeToServer(form, locale) {
    console.log("addGradeToServer(form, locale) called, locale = " + locale);

    const selectDateEn = "Select a date";
    const selectDateUa = '\u041E\u0431\u0435\u0440\u0456\u0442\u044C \u0434\u0430\u0442\u0443';
    const selectSubjectEn = "Select a subject";
    const selectSubjectUa = '\u041E\u0431\u0435\u0440\u0456\u0442\u044C \u043F\u0440\u0435\u0434\u043C\u0435\u0442';
    const enterMarkEn = "Enter a mark";
    const enterMarkUa = '\u0412\u0432\u0435\u0434\u0456\u0442\u044C \u043E\u0446\u0456\u043D\u043A\u0443';
    const markNegativeEn = "Mark can not be less than zero";
    const markNegativeUa = '\u041E\u0446\u0456\u043D\u043A\u043E\u044E \u043D\u0435 \u043C\u043E\u0436\u0435 \u0431\u0443\u0442\u0438 \u0432\u0456\u0434\'\u0454\u043C\u043D\u0435 \u0437\u043D\u0430\u0447\u0435\u043D\u043D\u044F';
    let statusMessage = $("#statusMessage");
    statusMessage.html("");
    $('#modalAddGrade').modal({show: true});

    let date = $("#date").val();
    let subjectId = $("#selectedSubject").val();
    let mark = $("#mark").val();

    if(date === null || date === undefined || date === ''){
        statusMessage.html(locale === 'en' ? selectDateEn : selectDateUa);
    }else if(subjectId === null || subjectId === undefined || subjectId === ''){
        statusMessage.html(locale === 'en' ? selectSubjectEn : selectSubjectUa);
    }else if(mark === null || mark === undefined || mark === ''){
        statusMessage.html(locale === 'en' ? enterMarkEn : enterMarkUa);
    }else if(mark < 0){
        statusMessage.html(locale === 'en' ? markNegativeEn : markNegativeUa);
    }else {
        $.ajax({
            url: $(location).attr("protocol") + "//" + $(location).attr("host") + "/create/grade",
            type: 'POST',
            data: $(form).serialize()
        }).done(function(data){
            statusMessage.attr("class", "alert-success");
            statusMessage.html(data["statusMessage"]);
        });
    }
}
