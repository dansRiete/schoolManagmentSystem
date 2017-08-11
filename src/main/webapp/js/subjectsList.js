/**
 * Created by Aleks on 03.08.2017.
 */
$(document).ready(
    $('#myModalAddSubject').on('hidden.bs.modal', function () {
        window.location.reload(true);
    })
);

function addSubject(form) {
    let link = $(location).attr("protocol") + "//" + $(location).attr("host") + "/locale";
    const DEFAULT_LOCALE = 'en';
    $.ajax({url: link, type: "GET"})
        .fail(function () {
            addSubjectOnServer(form, 'en');
        })
        .done(function (msg) {
            let locale = msg['locale_language'];
            addSubjectOnServer(form, locale === undefined || locale === null ? DEFAULT_LOCALE : locale);
        });
}

function addSubjectOnServer(form, locale) {
    console.log("addSubject()");
    let title = $("#newSubjectTitle").val();
    let subjectTitleDiv = $("#newSubjectTitleDiv");
    let subjectTitleErrorSpan = $("#subject-title-error");
    if (title === null || title === undefined || title === '') {
        console.log("Subject title is null");
        subjectTitleDiv.attr('class', 'form-group row has-error');
        subjectTitleErrorSpan.html(locale == 'ua' ? '\u0412\u0432\u0435\u0434\u0456\u0442\u044C \u043D\u0430\u0437\u0432\u0443 \u043F\u0440\u0435\u0434\u043C\u0435\u0442\u0430' : "Enter a title");
    } else if (title.indexOf(' ') >= 0) {
        console.log("Subject title contains spaces");
        subjectTitleDiv.attr('class', 'form-group row has-error');
        subjectTitleErrorSpan.html(locale == 'ua' ? '\u041D\u0430\u0437\u0432\u0430 \u043D\u0435 \u043C\u043E\u0436\u0435 \u043C\u0456\u0441\u0442\u0438\u0442\u0438 \u043F\u0440\u043E\u0431\u0456\u043B\u0456\u0432' : "Title can not contain spaces");
    } else {
        $.ajax({
            url: $(location).attr("protocol") + "//" + $(location).attr("host") + "/create/subject",
            type: 'POST',
            data: $(form).serialize()
        }).done(function (data) {
            console.log("add subject ajax done");
            if (data["status"] === 'success') {
                console.log('server responds success');
                $('#myModalAddSubject').modal('hide');
                /*subjectTitleDiv.attr('class', 'form-group row has-success');
                subjectTitleErrorSpan.attr('class', 'help-inline success-span');
                subjectTitleErrorSpan.html(data["message"]);*/
            } else {
                console.log('server responds error');
                subjectTitleDiv.attr('class', 'form-group row has-error');
                subjectTitleErrorSpan.attr('class', 'help-inline error-span');
                subjectTitleErrorSpan.html(data["message"]);
            }
        });
    }

}

function deleteSubject(id) {
    console.log('id = ' + id);
    let link = $(location).attr("protocol") + "//" + $(location).attr("host") + "/fetchBySubject";
    const someProblemsEn = "Deleting was failed"
    const someProblemsUa = "\u0412\u0438\u0434\u0430\u043B\u0435\u043D\u043D\u044F \u043D\u0435 \u0432\u0434\u0430\u043B\u043E\u0441\u044F"
    const tryLaterEn = "Something went wrong during deleting the subject, please try again later"
    const tryLaterUa = "\u041F\u0456\u0434 \u0447\u0430\u0441 \u0432\u0438\u0434\u0430\u043B\u0435\u043D\u043D\u044F \u043F\u0440\u0435\u0434\u043C\u0435\u0442\u0443 \u0441\u0442\u0430\u043B\u0430\u0441\u044F \u043F\u043E\u043C\u0438\u043B\u043A\u0430. \u0411\u0443\u0434\u044C-\u043B\u0430\u0441\u043A\u0430 \u0441\u043F\u0440\u043E\u0431\u0443\u0439\u0442\u0435 \u043F\u0456\u0437\u043D\u0456\u0448\u0435."
    $.ajax({url: link, type: "POST", data: {'selectedSubjectId': id}})
        .fail(function () {
            console.log("Sending form failed");
            $("#modalDS-title-text").html(someProblemsEn);
            $("#modalDS-body-text").html(tryLaterEn);
        })
        .done(function (msg) {
            if (msg["gradesOnSubjectCount"] === 0) {
                $("#deleteSubjectForm_" + id).submit();
            } else {
                $("#modalDeleteSubjId").val(id);
                $("#myModalDeleteSubject").modal({show: true});
            }
        });
}

function showAddSubjectModal() {

    let subjectTitleDiv = $("#newSubjectTitleDiv");
    let subjectTitleErrorSpan = $("#subject-title-error");
    subjectTitleDiv.attr('class', 'form-group row');
    $("#newSubjectTitle").val('');
    subjectTitleErrorSpan.html('');
    $('#myModalAddSubject').modal({show: true})
}
