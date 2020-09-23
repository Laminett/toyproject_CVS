function getToday() {
    var date = new Date();

    var yyyy = date.getFullYear().toString();
    var MM = date.getMonth()+1;
    var dd = date.getDate().toString();

    if(MM < 10){
        MM = "0"+MM;
    }

    return yyyy+MM+dd;
}