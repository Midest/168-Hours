/* Label name change for custom file input */
function changeFileName() {
    var inp = $(document).find('#fileInput')
    var p = inp.val().split("\\");
    inp.parent().find('label').html(p[p.length-1]);
}
