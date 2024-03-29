'use strict';

/* eslint-disable require-jsdoc, no-unused-vars */


var CalendarList = [];
let ScheduleList = [];
let calendarType = 0; // booking

function CalendarInfo() {
    this.id = null;
    this.name = null;
    this.checked = true;
    this.color = null;
    this.bgColor = null;
    this.borderColor = null;
    this.dragBgColor = null;
}
function getIndex(id){
    for (let current = 0; current < ScheduleList.length; current++){
        if(id==ScheduleList[current].id){
            return ScheduleList[current].stepIndex;
        }
    }
}
function addCalendar(calendar) {
    CalendarList.push(calendar);
}
function bookSchedule(schedule) {
 ScheduleList.push(schedule);
}
function getCurrentSchedule() {
    for (let current = 0; current < ScheduleList.length; current++){
        if(ScheduleList[current].stepIndex==calendarUpdate.stepIndex){
        return ScheduleList[current];
        }
    }
}

function findCalendar(id) {
    var found;

    CalendarList.forEach(function(calendar) {
        if (calendar.id === id) {
            found = calendar;
        }
    });

    return found || CalendarList[0];
}

function hexToRGBA(hex) {
    var radix = 16;
    var r = parseInt(hex.slice(1, 3), radix),
        g = parseInt(hex.slice(3, 5), radix),
        b = parseInt(hex.slice(5, 7), radix),
        a = parseInt(hex.slice(7, 9), radix) / 255 || 1;
    var rgba = 'rgba(' + r + ', ' + g + ', ' + b + ', ' + a + ')';

    return rgba;
}

(function() {
    //User steps
    let calendar = new CalendarInfo();
    calendar.id = 1;
    calendar.name = translations.personalsteps;
    calendar.color = '#ffffff';
    calendar.bgColor = '#0275d8';
    calendar.dragBgColor = '#0275d8';
    calendar.borderColor = '#0275d8';
    addCalendar(calendar);

    //other steps
    calendar = new CalendarInfo();
    calendar.id = 2;
    calendar.name = translations.othersteps ;
    calendar.color = '#ffffff';
    calendar.bgColor = '#292b2c';
    calendar.dragBgColor = '#292b2c';
    calendar.borderColor = '#292b2c';
    addCalendar(calendar);
    //succes
    calendar = new CalendarInfo();
    calendar.id = 3;
    calendar.name = translations.success;
    calendar.color = '#ffffff';
    calendar.bgColor = '#5cb85c';
    calendar.dragBgColor = '#5cb85c';
    calendar.borderColor = '#5cb85c';
    addCalendar(calendar);
    //Error
    calendar = new CalendarInfo();
    calendar.id = 4;
    calendar.name = translations.problem;
    calendar.color = '#ffffff';
    calendar.bgColor = '#d9534f';
    calendar.dragBgColor = '#d9534f';
    calendar.borderColor = '#d9534f';
    addCalendar(calendar);
    //suggestion
    calendar = new CalendarInfo();
    calendar.id = 5;
    calendar.name = translations.suggestion;
    calendar.color = '#ffffff';
    calendar.bgColor = '#f0ad4e';
    calendar.dragBgColor = '#f0ad4e';
    calendar.borderColor = '#f0ad4e';
    addCalendar(calendar);
})();


function ScheduleInfo() {
    this.id = null;
    this.calendarId = null;

    this.title = null;
    this.body = null;
    this.isAllday = false;
    this.start = null;
    this.end = null;
    this.category = '';
    this.dueDateClass = '';
    this.stepIndex =-1;
    this.color = null;
    this.bgColor = null;
    this.dragBgColor = null;
    this.borderColor = null;
    this.customStyle = '';

    this.isFocused = false;
    this.isPending = false;
    this.isVisible = true;
    this.isReadOnly = false;
    this.goingDuration = 0;
    this.comingDuration = 0;
    this.recurrenceRule = '';
    this.state = '';

    this.raw = {
        memo: '',
        hasToOrCc: false,
        hasRecurrenceRule: false,
        location: null,
        class: 'public', // or 'private'
        creator: {
            name: '',
            avatar: '',
            company: '',
            email: '',
            phone: ''
        }
    };
}
function addDevices(possibleDevices) {
    $('#deviceTypeDropdown').find('option').remove();

    if(possibleDevices.length>0&&checkContinuity(calendarUpdate.stepIndex,newSchedule,false,false).ok) {
        for (let current = 0; current < possibleDevices.length; current++) {
            const optionText = devices[possibleDevices[current]]['devicename'];
            const optionValue = devices[possibleDevices[current]]['id'];
            $('#deviceTypeDropdown').append($('<option>').val(optionValue).text(optionText));
        }
        document.getElementById('selectStep').disabled = false;
    }else{
        document.getElementById('selectStep').disabled = true;
    }
}
function checkOverlap(schedule,personalAllowed) {
    let possibleDevices=[];
    var deviceTypeId = allExperiments[calendarUpdate.experimentIndex]['stepTypes'][calendarUpdate.stepIndex]['deviceType']['id'];
    for (let current = 0; current < devices.length; current++) {
        if (devices[current]['deviceType']['id'] == deviceTypeId) { //found possible device, looking for use
            let deviceId = devices[current]['id'];
            var scheduleStart = new Date(schedule.start.getFullYear(), schedule.start.getMonth(), schedule.start.getDate(), schedule.start.getHours(), schedule.start.getMinutes());
            var scheduleEnd = new Date(schedule.end.getFullYear(), schedule.end.getMonth(), schedule.end.getDate(), schedule.end.getHours(), schedule.end.getMinutes());
            let overlap = false;
            //first check other steps
            for(var currentStep=0;currentStep<otherSteps.length;currentStep++){
                if(otherSteps[currentStep]['device']['id']==deviceId){ //found step booked of same device
                    var stepStart = new Date(otherSteps[currentStep]['start']+ 'T'+otherSteps[currentStep]['startHour']);
                    var stepEnd = new Date(otherSteps[currentStep]['end']+ 'T'+otherSteps[currentStep]['endHour']);

                    //check if date overlaps with schedule
                    if(scheduleStart.getTime()<stepStart.getTime()&&scheduleEnd.getTime()>stepStart.getTime() ){
                        overlap = true;
                    }else if(scheduleEnd.getTime()>stepEnd.getTime() &&scheduleStart.getTime()<stepEnd.getTime() ){
                        overlap = true;
                    }else if(scheduleStart.getTime()>=stepStart.getTime()&&scheduleEnd.getTime()<=stepEnd.getTime()){
                        overlap = true;
                    }
                }
            }
            //now check own steps
            if(personalAllowed){
                for (var currentStep = 0; currentStep < userSteps.length; currentStep++) {
                    if (userSteps[currentStep]['device']['id'] == deviceId) { //found step booked of same device
                        stepStart = new Date(userSteps[currentStep]['start'] + 'T' + userSteps[currentStep]['startHour']);
                        stepEnd = new Date(userSteps[currentStep]['end'] + 'T' + userSteps[currentStep]['endHour']);
                        //check if date overlaps with schedule

                        if(scheduleStart.getTime()<stepStart.getTime()&&scheduleEnd.getTime()>stepStart.getTime() ){
                            overlap = true;
                        }else if(scheduleEnd.getTime()>stepEnd.getTime() &&scheduleStart.getTime()<stepEnd.getTime() ){
                            overlap = true;
                        }else if(scheduleStart.getTime()>=stepStart.getTime()&&scheduleEnd.getTime()<=stepEnd.getTime()){
                            overlap = true;
                        }
                    }
                }
            }else{
                for (var currentStep = 0; currentStep < userSteps.length; currentStep++) {
                        stepStart = new Date(userSteps[currentStep]['start'] + 'T' + userSteps[currentStep]['startHour']);
                        stepEnd = new Date(userSteps[currentStep]['end'] + 'T' + userSteps[currentStep]['endHour']);
                        //check if date overlaps with schedule
                    if(scheduleStart.getTime()<stepStart.getTime()&&scheduleEnd.getTime()>stepStart.getTime() ){
                        overlap = true;
                    }else if(scheduleEnd.getTime()>stepEnd.getTime() &&scheduleStart.getTime()<stepEnd.getTime() ){
                        overlap = true;
                    }else if(scheduleStart.getTime()>=stepStart.getTime()&&scheduleEnd.getTime()<=stepEnd.getTime()){
                        overlap = true;
                    }
                }
            }
            if(!overlap){
                possibleDevices.push(current);
            }
        }
    }
    return possibleDevices;
}

function checkContinuity(stepindex,schedule,withinOfficehours,isGrayout) {
    let stepType =  allExperiments[calendarUpdate.experimentIndex]['stepTypes'][stepindex];
    //Check length
    if(!isGrayout) {
        if (allExperiments[calendarUpdate.experimentIndex]['stepTypes'][stepindex]['hasFixedLength']) {
            var start = new Date(schedule.start.getFullYear(), schedule.start.getMonth(), schedule.start.getDate(), schedule.start.getHours(), schedule.start.getMinutes());
            var end = new Date(schedule.end.getFullYear(), schedule.end.getMonth(), schedule.end.getDate(), schedule.end.getHours(), schedule.end.getMinutes());
            var lengthMinutes = parseInt((end - start) / 1000 / 60);
            var requiredLength = (parseInt(allExperiments[calendarUpdate.experimentIndex]['stepTypes'][stepindex]['fixedTimeHours']) * 60) + parseInt(allExperiments[calendarUpdate.experimentIndex]['stepTypes'][stepindex]['fixedTimeMinutes']);
            if (allExperiments[calendarUpdate.experimentIndex]['stepTypes'][stepindex]['fixedTimeType'] === "Equal") {
                if (lengthMinutes != requiredLength) {
                    return {
                        message: translations.lengthequal + allExperiments[calendarUpdate.experimentIndex]['stepTypes'][stepindex]['fixedTimeHours'] + "h " + allExperiments[calendarUpdate.experimentIndex]['stepTypes'][stepindex]['fixedTimeMinutes'] + "m"+translations.long,
                        ok: false,
                    }
                }
            } else if (allExperiments[calendarUpdate.experimentIndex]['stepTypes'][stepindex]['fixedTimeType'] === "At least") {
                if (lengthMinutes < requiredLength) {
                    return {
                        message: translations.lengthatleast + allExperiments[calendarUpdate.experimentIndex]['stepTypes'][stepindex]['fixedTimeHours'] + "h " + allExperiments[calendarUpdate.experimentIndex]['stepTypes'][stepindex]['fixedTimeMinutes'] + "m"+translations.long,
                        ok: false,
                    }
                }
            } else if (allExperiments[calendarUpdate.experimentIndex]['stepTypes'][stepindex]['fixedTimeType'] === "At most") {
                if (lengthMinutes > requiredLength) {
                    return {
                        message: translations.lengthatmost + allExperiments[calendarUpdate.experimentIndex]['stepTypes'][stepindex]['fixedTimeHours'] + "h " + allExperiments[calendarUpdate.experimentIndex]['stepTypes'][stepindex]['fixedTimeMinutes']+ "m"+translations.long,
                        ok: false,
                    }
                }
            }
        }
    }



    if(stepindex-1>=0&&filledInSteps[stepindex-1]){

        let previousSchedule = filledInSteps[stepindex-1];
        //check not before previous step
        let previousStepType = allExperiments[calendarUpdate.experimentIndex]['stepTypes'][stepindex-1];
        var firstDate = new Date(previousSchedule.end.getFullYear(), previousSchedule.end.getMonth(), previousSchedule.end.getDate(), previousSchedule.end.getHours(), previousSchedule.end.getMinutes());
        //add hours and minutes of continuity
        var secondDate = new Date(schedule.end.getFullYear(), schedule.end.getMonth(), schedule.end.getDate(), schedule.end.getHours(), schedule.end.getMinutes());
        if(secondDate.getTime()<firstDate.getTime()){
            return {
                message: translations.beforeprevious,
                ok:false,
            }
        }
        firstDate.setHours(firstDate.getHours()+parseInt(previousStepType['continuity']['hours']));
        firstDate.setMinutes(firstDate.getMinutes()+parseInt(previousStepType['continuity']['minutes']));


        //Continuity
        //Hard
        previousStepType = allExperiments[calendarUpdate.experimentIndex]['stepTypes'][stepindex-1];
        firstDate = new Date(previousSchedule.end.getFullYear(), previousSchedule.end.getMonth(), previousSchedule.end.getDate(), previousSchedule.end.getHours(), previousSchedule.end.getMinutes());

        if(previousStepType['continuity']['type']=="Hard"){
            if(previousStepType['continuity']['directionType']=="After"){
                //add hours and minutes of continuity
                firstDate.setHours(firstDate.getHours()+parseInt(previousStepType['continuity']['hours']));
                firstDate.setMinutes(firstDate.getMinutes()+parseInt(previousStepType['continuity']['minutes']));
                secondDate = new Date(schedule.start.getFullYear(), schedule.start.getMonth(), schedule.start.getDate(), schedule.start.getHours(), schedule.start.getMinutes());
                // console.log(secondDate);

                if(firstDate.getTime()!=secondDate.getTime()){
                return {
                    message: translations.hardafterone+' '+previousStepType['continuity']['hours']+"h"+previousStepType['continuity']['minutes']+"m"+' '+translations.hardaftertwo,
                    ok:false,
                    }
                }
            }else {
                secondDate = new Date(schedule.start.getFullYear(), schedule.start.getMonth(), schedule.start.getDate(), schedule.start.getHours(), schedule.start.getMinutes());
//add hours and minutes of continuity
                firstDate.setHours(firstDate.getHours()-parseInt(previousStepType['continuity']['hours']));
                firstDate.setMinutes(firstDate.getMinutes()-parseInt(previousStepType['continuity']['minutes']));
                if(firstDate.getTime()!=secondDate.getTime()){
                    return {
                        message:translations.hardbeforeone+' '+previousStepType['continuity']['hours']+"h"+previousStepType['continuity']['minutes']+"m"+' '+translations.hardbeforetwo,
                        ok:false,
                    }
                }
            }
        }

    //soft min
    if(previousStepType['continuity']['type']=="Soft (at least)"){
        if(previousStepType['continuity']['directionType']=="After"){
            firstDate.setHours(firstDate.getHours()+parseInt(previousStepType['continuity']['hours']));
            firstDate.setMinutes(firstDate.getMinutes()+parseInt(previousStepType['continuity']['minutes']));
            if(!(secondDate.getTime()>firstDate.getTime())) {
                return {
                    message: translations.atleastafterone+' ' + previousStepType['continuity']['hours']+"h"+previousStepType['continuity']['minutes']+"m"+' ' +translations.atleastaftertwo,
                    ok: false,
                }
            }
        }else {
            firstDate = new Date(previousSchedule.end.getFullYear(), previousSchedule.end.getMonth(), previousSchedule.end.getDate(), previousSchedule.end.getHours(), previousSchedule.end.getMinutes());

            secondDate = new Date(schedule.start.getFullYear(), schedule.start.getMonth(), schedule.start.getDate(), schedule.start.getHours(), schedule.start.getMinutes());
            firstDate.setHours(firstDate.getHours()-parseInt(previousStepType['continuity']['hours']));
            firstDate.setMinutes(firstDate.getMinutes()-parseInt(previousStepType['continuity']['minutes']));
            if(!(secondDate.getTime()>=firstDate.getTime())) {
                return {
                    message: translations.atleastbeforeone +' '+ previousStepType['continuity']['hours'] +"h"+previousStepType['continuity']['minutes']+"m"+' '+ translations.atleastbeforetwo,
                    ok: false,
                }
            }

        }
    }

    //soft most
    if(previousStepType['continuity']['type']=="Soft (at most)"){
        if(previousStepType['continuity']['directionType']=="After"){
            if(!(secondDate.getTime()<=firstDate.getTime())) {
                return {
                    message: translations.atmostafterone +' '+ previousStepType['continuity']['hours'] +"h"+previousStepType['continuity']['minutes']+"m"+' '+ translations.atmostaftertwo,
                    ok: false,
                }
            }
        }else {
            secondDate = new Date(schedule.start.getFullYear(), schedule.start.getMonth(), schedule.start.getDate(), schedule.start.getHours(), schedule.start.getMinutes());
            firstDate.setHours(firstDate.getHours()-parseInt(previousStepType['continuity']['hours']));
            firstDate.setMinutes(firstDate.getMinutes()-parseInt(previousStepType['continuity']['minutes']));
            if(!(secondDate.getTime()<=firstDate.getTime())) {
                return {
                    message: translations.atmostbeforeone +' ' +previousStepType['continuity']['hours']+"h"+previousStepType['continuity']['minutes']+"m"+' '  + translations.atmostbeforetwo,
                    ok: false,
                }
            }
        }
    }
    }

    //OvernightUse
    if(!stepType['deviceType']['overnightuse']){
        if(schedule.start.getDate()!=schedule.end.getDate())
            if(schedule.end.getHours()!=0&&schedule.end.getMinutes()!=0){
                return {
                    message: translations.overnight,
                    ok:false,
                }
            }
    }

    //Weekend
    if(schedule.start.getDay()!=0&&schedule.start.getDay()!=6){
        if(schedule.end.getDay()!=0&&schedule.end.getDay()!=6){

        }else{
            if(schedule.end.getDay()==6&&schedule.end.getHours()!=0&&schedule.end.getMinutes()!=0){
                return {
                    message: translations.overnight,
                    ok:false,
                }
            }
        }
    }else{
        return {
            message: translations.weekend,
            ok:false,
        }
    }
    //Hollidays
    var str = schedule.start.getFullYear().toString()+ "-" + ("0" + (schedule.start.getMonth() + 1)).slice(-2) + "-" + ("0" + (schedule.start.getDate())).slice(-2) ;
    for (let current = 0; current < holidays.length; current++) {
        if (holidays[current]['date'] == str) {
            return {
                message: translations.holiday,
                ok: false,
            }
        }
    }
    let startTime = schedule.start.getMinutes()+schedule.start.getHours()*60;
    //add hours and minutes of continuity
    let endTime = schedule.end.getMinutes()+schedule.end.getHours()*60;

    if(withinOfficehours){
        if (startTime >= parseInt(officeHours['startHour']) * 60 + parseInt(officeHours['startMinute'])&& startTime <= parseInt(officeHours['endHour'])  * 60 + parseInt(officeHours['endMinute'])&& schedule.start.getDay() != 6 && schedule.start.getDay() != 0) {

        } else {
            return {
                message:translations.outofofficehours,
                ok: false,
            }
        }
    }


    //OpeningsHours\
    if(userAccessRights.includes("Bachelorstudent")) {
        if (startTime >= parseInt(officeHours['startHour'])  * 60 + parseInt(officeHours['startMinute'])  && startTime <= parseInt(officeHours['endHour'])  * 60 + parseInt(officeHours['endMinute'])  && schedule.start.getDay() != 6 && schedule.start.getDay() != 0) {
            if (endTime >= parseInt(officeHours['startHour'])  * 60 + parseInt(officeHours['startMinute'])&& endTime <= parseInt(officeHours['endHour'])  * 60+ parseInt(officeHours['endMinute']) && schedule.end.getDay() != 0 && schedule.end.getDay() != 6) {
            } else {
                return {
                    message: translations.outofofficehours,
                    ok: false,
                }
            }
        } else {
            return {
                message: translations.outofofficehours,
                ok: false,
            }
        }
    }

    let today = new Date();
    if(schedule.start.getTime()<today.getTime()){
        return {
            message: translations.cantbookpast,
            ok:false,
        }
    }
    return {
        message: translations.noproblems,
    ok:true,
}}

function createSuggestionSchedule(start,end,calendar) {
    let step = allExperiments[calendarUpdate.experimentIndex]['stepTypes'][calendarUpdate.stepIndex];
    let schedule = new ScheduleInfo();
    schedule.id = chance.guid();
    schedule.calendarId = calendar.id;
    schedule.title = translations.bodysuggestion
    schedule.body = translations.step+' '+(calendarUpdate.stepIndex+1)+': Experiment '+allExperiments[calendarUpdate.experimentIndex]['experimentTypeName']+', \nDevice = '+step['deviceType']['deviceTypeName'];
    schedule.isReadOnly = false;
    schedule.start = start;
    schedule.end = end;
    schedule.color = calendar.color;
    schedule.bgColor = '#f0ad4e';
    schedule.dragBgColor = '#f0ad4e';
    schedule.borderColor = '#f0ad4e';
    schedule.category = 'time';
    return schedule
}

function generateSchedule(viewName, renderStart, renderEnd) {
    ScheduleList = [];
    //Add all users steps
    userSteps.forEach(function (step) {
        let schedule = new ScheduleInfo();
            let calendar = findCalendar(String(step['device']['deviceType']['id']));
            schedule.id = chance.guid();
            schedule.calendarId = calendar.id;
            schedule.title = step['device']['deviceType']['deviceTypeName'];
            schedule.body = translations.name +': ' + step['device']['devicename'] + "<br>"
                + translations.type+':  ' + step['device']['deviceType']['deviceTypeName'] + "<br>"
                + translations.comment+':  ' + step['device']['comment'] + "<br>";
            ;
        // translations.user +': ' + step['user']['username'] + "<br>"

        schedule.isReadOnly = true;
            schedule.start = new Date(step['start'] + 'T' + step['startHour']);
            schedule.end = new Date(step['end'] + 'T' + step['endHour']);
            schedule.isReadOnly = true;
            schedule.color = calendar.color;
                schedule.bgColor = '#0275d8';
                schedule.dragBgColor = '#0275d8';
                schedule.borderColor = '#0275d8';
            schedule.category = 'time';
            ScheduleList.push(schedule);
        // }
    })

    //Add all other steps of devicetype
    otherSteps.forEach(function (step) {

        if(step['device']['deviceType']['id'] === allExperiments[calendarUpdate.experimentIndex]['stepTypes'][calendarUpdate.stepIndex]['deviceType']['id']) {
            let schedule = new ScheduleInfo();
            let calendar = findCalendar(String(step['device']['deviceType']['id']));
            schedule.id = chance.guid();
            schedule.calendarId = calendar.id;
            schedule.title = step['device']['deviceType']['deviceTypeName'];
            schedule.body =  translations.name +': ' + step['device']['devicename'] + "<br>"
                + translations.type+':  ' + step['device']['deviceType']['deviceTypeName'] + "<br>"
                + translations.comment+':  ' + step['device']['comment'] + "<br>";
            ;
            schedule.isReadOnly = true;
            schedule.start = new Date(step['start'] + 'T' + step['startHour']);
            schedule.end = new Date(step['end'] + 'T' + step['endHour']);
            schedule.color = calendar.color;
            schedule.bgColor = '#292b2c';
            schedule.dragBgColor = '#292b2c';
            schedule.borderColor = '#292b2c';
            schedule.category = 'time';
            ScheduleList.push(schedule);
        }
    })

    //Add all current filled in steps
    let numberOfSteps = allExperiments[calendarUpdate.experimentIndex]['stepTypes'].length;
    for (let current = 0; current < numberOfSteps; current++){
        let ok = true;
        let step = allExperiments[calendarUpdate.experimentIndex]['stepTypes'][current];
        let schedule = new ScheduleInfo();
        let calendar = findCalendar(String(step['deviceType']['id']));
        schedule.id = chance.guid();
        schedule.calendarId = calendar.id;
        schedule.title = translations.step+(current+1)+': Experiment '+allExperiments[calendarUpdate.experimentIndex]['experimentTypeName']+', \nDevice = '+step['deviceType']['deviceTypeName'];
        if(calendarUpdate.stepIndex==current){
            schedule.isReadOnly = false;
        }else{
            schedule.isReadOnly = true;
        }
        if(document.getElementById('startDate' + current + '')) {
            var start = document.getElementById('startDate' + current + '').value;
            if (start != "") {
                var startHour = document.getElementById('startHour' + current + '').value;
                if (start != "") {
                    schedule.start = new Date(start + 'T' + startHour);
                } else {
                    ok = false;
                }
            } else {
                ok = false;
            }
            var end = document.getElementById('endDate' + current + '').value;
            if (end != "") {
                var endHour = document.getElementById('endHour' + current + '').value;
                if (end != "") {
                    schedule.end = new Date(end + 'T' + endHour);
                } else {
                    ok = false;
                }
            } else {
                ok = false;
            }

        schedule.color = calendar.color;
        schedule.category = 'time';
        schedule.stepIndex = current;
        }else{
            ok=false;
        }
        if(ok){
            let check = checkContinuity(current,schedule,false,false);
            if(check.ok) {
                schedule.bgColor = '#5cb85c';
                schedule.dragBgColor = '#5cb85c';
                schedule.borderColor = '#5cb85c';
                schedule.body=check.message;
            }else{
                schedule.bgColor = '#d9534f';
                schedule.dragBgColor = '#d9534f';
                schedule.borderColor = '#d9534f';
                schedule.body=check.message;
            }
            ScheduleList.push(schedule);
        }
        if(calendarUpdate.stepIndex==current){
            newSchedule =schedule;
        }
        if(ok){
            filledInSteps[current] = schedule;
        }else{
            filledInSteps[current] = null;
        }

    }
    if(filledInSteps[calendarUpdate.stepIndex]!=null){

        addDevices( checkOverlap(filledInSteps[calendarUpdate.stepIndex],document.getElementById("allowOverlap").checked))
    }
    if(suggestion){
        ScheduleList.push(suggestion);
    }
}

