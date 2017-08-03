/**
 * Created by Aleks on 03.08.2017.
 */
let app = {
    showDeleteSubjModal(){
        $('#myModalDeleteSubject').modal({show: true});
    }
}
function addFunction(id) {
    let link = $(location).attr("protocol") + "//" + $(location).attr("host") + "/fetchBySubject";
    $.ajax({url: link,type: "POST", data: {'selectedSubjectIdParameter': id}})
        .fail(function () {
            console.log("Sending form failed")
        })
        .done(function (msg) {
            if (msg["gradesOnSubjectCount"] === 0) {
                $("#deleteSubjectForm_"+id).submit();
            } else {
                $("#modalDeleteSubjId").val(id);
                $("#modalDS-title-text").html('There is one or more grades in this subject');
                $("#modalDS-body-text").html('Are you sure you want to delete this subject? All related to this subject grades will be deleted as well!');
                app.showDeleteSubjModal();
            }
        });
}