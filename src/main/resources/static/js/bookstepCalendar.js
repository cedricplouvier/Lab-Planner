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
    calendar.name = "Personal calendar items";
    calendar.color = '#ffffff';
    calendar.bgColor = '#0275d8';
    calendar.dragBgColor = '#0275d8';
    calendar.borderColor = '#0275d8';
    addCalendar(calendar);

    //other steps
    calendar = new CalendarInfo();
    calendar.id = 2;
    calendar.name = "Other devices used";
    calendar.color = '#ffffff';
    calendar.bgColor = '#292b2c';
    calendar.dragBgColor = '#292b2c';
    calendar.borderColor = '#292b2c';
    addCalendar(calendar);
    //succes
    calendar = new CalendarInfo();
    calendar.id = 3;
    calendar.name = "Success";
    calendar.color = '#ffffff';
    calendar.bgColor = '#5cb85c';
    calendar.dragBgColor = '#5cb85c';
    calendar.borderColor = '#5cb85c';
    addCalendar(calendar);
    //Error
    calendar = new CalendarInfo();
    calendar.id = 4;
    calendar.name = "Problem";
    calendar.color = '#ffffff';
    calendar.bgColor = '#d9534f';
    calendar.dragBgColor = '#d9534f';
    calendar.borderColor = '#d9534f';
    addCalendar(calendar);
    //suggestion
    calendar = new CalendarInfo();
    calendar.id = 5;
    calendar.name = "suggestion";
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
function checkOverlap() {
    $('#deviceTypeDropdown').find('option').remove();
    var deviceTypeId = allExperiments[calendarUpdate.experimentIndex]['stepTypes'][calendarUpdate.stepIndex]['deviceType']['id'];
    for (var current = 0; current < devices.length; current++) {
        if (devices[current]['deviceType']['id'] == deviceTypeId) { //found possible device, looking for use
            let deviceId = devices[current]['id'];
            var scheduleStart = new Date(newSchedule.start.getFullYear(), newSchedule.start.getMonth(), newSchedule.start.getDate(), newSchedule.start.getHours(), newSchedule.start.getMinutes());
            var scheduleEnd = new Date(newSchedule.end.getFullYear(), newSchedule.end.getMonth(), newSchedule.end.getDate(), newSchedule.end.getHours(), newSchedule.end.getMinutes());

            let overlap = false;

            //first check other steps
            for(var currentStep=0;currentStep<otherSteps.length;currentStep++){
                if(otherSteps[currentStep]['device']['id']==deviceId){ //found step booked of same device
                    var stepStart = new Date(otherSteps[currentStep]['start']+ 'T'+otherSteps[currentStep]['startHour']);
                    var stepEnd = new Date(otherSteps[currentStep]['end']+ 'T'+otherSteps[currentStep]['endHour']);

                    //check if date overlaps with schedule
                    if(stepStart>scheduleStart&&stepStart<scheduleEnd){
                        overlap = true;
                    }else if(stepEnd>scheduleStart&&stepEnd<scheduleEnd){
                        overlap = true;
                    }
                }
            }
            //now check own steps
            //first check other steps
            for(var currentStep=0;currentStep<userSteps.length;currentStep++){
                if(userSteps[currentStep]['device']['id']==deviceId){ //found step booked of same device
                    var stepStart = new Date(userSteps[currentStep]['start']+ 'T'+userSteps[currentStep]['startHour']);
                    var stepEnd = new Date(userSteps[currentStep]['end']+ 'T'+userSteps[currentStep]['endHour']);

                    //check if date overlaps with schedule
                    if(stepStart>scheduleStart&&stepStart<scheduleEnd){
                        overlap = true;
                    }else if(stepEnd>scheduleStart&&stepEnd<scheduleEnd){
                        overlap = true;
                    }
                }
            }

            if(!overlap){
                const optionText = devices[current]['devicename'];
                const optionValue = devices[current]['id'];
                $('#deviceTypeDropdown').append($('<option>').val(optionValue).text(optionText));
                document.getElementById('selectStep').disabled = false;

            }
        }
    }
}
function checkContinuity(stepindex,schedule) {
    let stepType =  allExperiments[calendarUpdate.experimentIndex]['stepTypes'][stepindex];
    if(stepindex-1>=0){

        let previousSchedule = ScheduleList[stepindex-1];
        for (let current = 0; current < ScheduleList.length; current++){
            if(ScheduleList[current].stepIndex==stepindex-1){

                previousSchedule= ScheduleList[current];
            }
        }



    //Continuity
    //Hard
        let previousStepType = allExperiments[calendarUpdate.experimentIndex]['stepTypes'][stepindex-1];
        var firstDate = new Date(previousSchedule.end.getFullYear(), previousSchedule.end.getMonth(), previousSchedule.end.getDate(), previousSchedule.end.getHours(), previousSchedule.end.getMinutes());
        //add hours and minutes of continuity
        firstDate.setHours(firstDate.getHours()+previousStepType['continuity']['hours']);
        firstDate.setMinutes(firstDate.getMinutes()+previousStepType['continuity']['minutes']);
        var secondDate = new Date(schedule.start.getFullYear(), schedule.start.getMonth(), schedule.start.getDate(), schedule.start.getHours(), schedule.start.getMinutes());

        if(previousStepType['continuity']['type']=="Hard"){
            if(firstDate.getTime()!=secondDate.getTime()){
            return {
                message: "This device requires a hard continuity, This step should be exactly "+previousStepType['continuity']['hours']+" hours after the previous step in the experiment.",
                ok:false,
            }
        }
    }
    //soft min
    if(previousStepType['continuity']['type']=="Soft (at least)"){
        if(!(secondDate.getTime()>=firstDate.getTime())) {
            return {
                message: "This device requires a soft (at least) continuity, This step should be at least more then " + previousStepType['continuity']['hours'] + " hours after the previous step in the experiment.",
                ok: false,
            }
        }
    }
    //soft most
    if(previousStepType['continuity']['type']=="Soft (at most)"){
        if(!(secondDate.getTime()<=firstDate.getTime())) {
            return {
                message: "This device requires a hard continuity, This step should be at most mort then " + previousStepType['continuity']['hours'] + " hours after the previous step in the experiment.",
                ok: false,
            }
        }
    }
    }
    //OvernightUse
    if(!stepType['deviceType']['overnightuse']){
        if(schedule.start.getDate()!=schedule.end.getDate())
        return {
            message: "This device cant be used overnight",
            ok:false,
        }
    }

    //Weekend
    if(schedule.start.getDay()!=0&&schedule.start.getDay()!=6){
        if(schedule.end.getDay()!=0&&schedule.end.getDay()!=6){

        }else{
            return {
                message: "This device cant be used in weekends",
                ok:false,
            }
        }
    }else{
        return {
            message: "This device cant be used in weekends",
            ok:false,
        }
    }
    //Hollidays

    var str = schedule.start.getFullYear().toString()+ "-" + ("0" + (schedule.start.getMonth() + 1)).slice(-2) + "-" + ("0" + (schedule.start.getDate())).slice(-2) ;
    for (let current = 0; current < holidays.length; current++) {
        if (holidays[current]['date'] == str) {
            return {
                message: "This device cant be used on a holiday",
                ok: false,
            }
        }
    }
    //OpeningsHours
    if(schedule.start.getHours()>=9&&schedule.start.getHours()<=17&&schedule.start.getDay()!=0&&schedule.start.getDay()!=6){
        if(schedule.end.getHours()>=9&&schedule.end.getHours()<=17&&schedule.end.getDay()!=0&&schedule.end.getDay()!=6){

        }else{
            return {
                message: "This device cant be used out of office hours",
                ok:false,
            }
        }
    }else{
        return {
            message: "This device cant be used out of office hours",
            ok:false,
        }
    }

    //check if there are available devices
    if($('#deviceTypeDropdown').children().length==0){
        return {
            message: "There are no available devices for this timeslot",
            ok:false,
        }
    }
    return {
        message: "No problems found",
    ok:true,
}}

function generateSchedule(viewName, renderStart, renderEnd) {
    ScheduleList = [];
    //Add all users steps
    userSteps.forEach(function (step) {
        // if(step['device']['deviceType']['id'] === allExperiments[calendarUpdate.experimentIndex]['stepTypes'][calendarUpdate.stepIndex]['deviceType']['id']) {
            let schedule = new ScheduleInfo();
            let calendar = findCalendar(String(step['device']['deviceType']['id']));
            schedule.id = chance.guid();
            schedule.calendarId = calendar.id;
            schedule.title = step['device']['deviceType']['deviceTypeName'];
            schedule.body = 'name:    ' + step['device']['devicename'] + "<br>"
                + 'type:    ' + step['device']['deviceType']['deviceTypeName'] + "<br>"
                + 'comment: ' + step['device']['comment'] + "<br>";
            ;
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

    //Add all other steps of devicetpye
    otherSteps.forEach(function (step) {
        if(step['device']['deviceType']['id'] === allExperiments[calendarUpdate.experimentIndex]['stepTypes'][calendarUpdate.stepIndex]['deviceType']['id']) {
            let schedule = new ScheduleInfo();
            let calendar = findCalendar(String(step['device']['deviceType']['id']));
            schedule.id = chance.guid();
            schedule.calendarId = calendar.id;
            schedule.title = step['device']['deviceType']['deviceTypeName'];
            schedule.body = 'name:    ' + step['device']['devicename'] + "<br>"
                + 'type:    ' + step['device']['deviceType']['deviceTypeName'] + "<br>"
                + 'comment: ' + step['device']['comment'] + "<br>";
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
        schedule.title = 'Step '+(current+1)+' of Experiment '+allExperiments[calendarUpdate.experimentIndex]['experimentTypeName']+', \nDevice = '+step['deviceType']['deviceTypeName'];
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
            let check = checkContinuity(current,schedule);
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
    }
}

