/**
 * Created by Aleks on 03.08.2017.
 */
$(document).ready(
    $('#myModalAddSubject').on('hidden.bs.modal', function () {
        window.location.reload(true);
    })
);

function addSubject(form) {
    console.log("addSubject()");
    let title = $("#newSubjectTitle").val();
    let subjectTitleDiv = $("#newSubjectTitleDiv");
    let subjectTitleErrorSpan = $("#subject-title-error");
    if (title === null || title === undefined || title === '') {
        console.log("Subject title is null");
        subjectTitleDiv.attr('class', 'form-group row has-error');
        subjectTitleErrorSpan.html("Enter a title");
    } else if (title.indexOf(' ') >= 0) {
        console.log("Subject title contains spaces");
        subjectTitleDiv.attr('class', 'form-group row has-error');
        subjectTitleErrorSpan.html("Title can not contain spaces");
    } else {
        $.ajax({
            url: $(location).attr("protocol") + "//" + $(location).attr("host") + "/create/subject",
            type: 'POST',
            data: $(form).serialize()
        }).done(function (data) {
            console.log("add subject ajax done");
            if (data["status"] === 'success') {
                console.log('server responds success');
                subjectTitleDiv.attr('class', 'form-group row has-success');
                subjectTitleErrorSpan.attr('class', 'help-inline success-span');
                subjectTitleErrorSpan.html(data["message"]);
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
    let link = $(location).attr("protocol") + "//" + $(location).attr("host") + "/fetchBySubject";
    $.ajax({url: link, type: "POST", data: {'selectedSubjectIdParameter': id}})
        .fail(function () {
            console.log("Sending form failed")
        })
        .done(function (msg) {
            if (msg["gradesOnSubjectCount"] === 0) {
                $("#deleteSubjectForm_" + id).submit();
            } else {
                $("#modalDeleteSubjId").val(id);
                // $("#modalDS-title-text").html('There is one or more grades in this subject');
                // $("#modalDS-body-text").html('Are you sure you want to delete this subject? All related to this subject grades will be deleted as well!');
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
    /*$('#myModalAddSubject').on('hidden.bs.modal', function () {
        window.location.reload(true);
    })*/
}
