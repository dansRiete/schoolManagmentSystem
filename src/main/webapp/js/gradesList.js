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
            let modalBody = msg["modal_body"];
            let modalTitle = msg["modal_title"];
            let requestedDate = msg["selectedDate"];
            let locale = msg["locale_language"] == null || msg["locale_language"] == undefined || msg["locale_language"] == '' ? 'en' : msg["locale_language"];
            const subjectEn = 'Subject'
            const subjectUa = '\u041F\u0440\u0435\u0434\u043C\u0435\u0442'
            const allSubjectsEn = 'all subjects';
            const allSubjectsUa = '\u0432\u0441\u0456 \u043F\u0440\u0435\u0434\u043C\u0435\u0442\u0438';
            const noGradesEn = 'There are no grades on requested conditions';
            const noGradesUa = '\u041F\u043E \u0432\u0430\u0448\u043E\u043C\u0443 \u0437\u0430\u043F\u0438\u0442\u0443 \u043D\u0435 \u0437\u043D\u0430\u0439\u0434\u0435\u043D\u043E \u0436\u043E\u0434\u043D\u043E\u0457 \u043E\u0446\u0456\u043D\u043A\u0438';
            const avgMarkOnEn = 'Average mark on';
            const avgMarkOnUa = '\u0421\u0435\u0440\u0435\u0434\u043D\u044F \u043E\u0446\u0456\u043D\u043A\u0430 \u043F\u043E';
            const dateEn = 'Date';
            const dateUa = '\u0414\u0430\u0442\u0430';
            const allDatesEn = 'all dates';
            const allDatesUa = '\u043F\u043E \u0432\u0441\u0456\u0445 \u0434\u0430\u0442\u0430\u0445';
            console.log('modalTitle = ' + modalTitle);
            console.log('modalBody = ' + modalBody);
            $("#modal-body-text").html(modalBody);
            $("#modal-title-text").html(modalTitle);
            $('#myModal').modal({show: true});
        });
}

function resetFilter(){
    let link = $(location).attr("protocol") + "//" + $(location).attr("host") + "/list/grades?selectedDate=&selectedSubjectId=";
    window.location.replace(link);
}

function addGrade(form) {
    let link = $(location).attr("protocol") + "//" + $(location).attr("host") + "/locale";
    const DEFAULT_LOCALE = 'en';
    $.ajax({url: link, type: "GET"})
        .fail(function () {
            addGradeToServer(form, 'en');
        })
        .done(function (msg) {
            let locale = msg['locale_language'];
            addGradeToServer(form, locale === undefined || locale === null ? DEFAULT_LOCALE : locale);
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
    let subjectId = $("#modalSelectedSubjectId").val();
    let mark = $("#mark").val();

    if(date === null || date === undefined || date === ''){
        statusMessage.html(locale === 'ua' ? selectDateUa : selectDateEn);
    }else if(subjectId === null || subjectId === undefined || subjectId === ''){
        statusMessage.html(locale === 'ua' ? selectSubjectUa : selectSubjectEn);
    }else if(mark === null || mark === undefined || mark === ''){
        statusMessage.html(locale === 'ua' ? enterMarkUa : enterMarkEn);
    }else if(mark < 0){
        statusMessage.html(locale === 'ua' ? markNegativeUa : markNegativeEn);
    }else {
        $.ajax({
            url: $(location).attr("protocol") + "//" + $(location).attr("host") + "/create/grade",
            type: 'POST',
            data: $(form).serialize()
        }).done(function(data){
            console.log(data['statusMessage']);
            if(!data['statusMessage'].startsWith('Error')){
                $('#modalAddGrade').modal('hide');
            }else {
                statusMessage.html(data['statusMessage']);
            }
        });
    }
}
