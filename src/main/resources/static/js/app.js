 'use strict';

/* eslint-disable require-jsdoc */
/* eslint-env jquery */
/* global moment, tui, chance */
/* global findCalendar, CalendarList, ScheduleList, generateSchedule */
let filledInSteps=[];
let preCalculatedSuggestions = [];
let newSchedule;
let suggestion;
let showOwnItems = true;
let showOtherItems = false;
var contextpath = document.getElementById('contextpath').value;
var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");
 // Prepend context path to all jQuery AJAX requests
 $.ajaxPrefilter(function( options, originalOptions, jqXHR ) {
     if (!options.crossDomain) {
         options.url = contextpath + options.url;
     }
     jqXHR.setRequestHeader('X-CSRF-Token', token);

 });
(function(window, Calendar) {
    let cal, resizeThrottled;
    let useCreationPopup = false;
    let useDetailPopup = true;
    let datePicker, selectedCalendar;
    if(calendarType==0){
        cal = new Calendar('#calendar', {
            defaultView: 'week',
            useCreationPopup: useCreationPopup,
            useDetailPopup: useDetailPopup,
            calendars: CalendarList,
            taskView: false,
            disableDblClick: true,
            disableClick: false,
            isReadOnly:false,
            week: {
                daynames: [translations.sun, translations.mon,translations.tue,translations.wed,translations.thu,translations.fri,translations.sat],
                startDayOfWeek: 1 // monday
            },
            scheduleView: ['time'],
            template: {
                time: function(schedule) {
                    return getTimeTemplate(schedule, false);
                },
                timegridDisplayPrimayTime: function(time) {
                    var hour = time.hour;
                    return hour +':00';
                }
            }
        });
    }else{
        cal = new Calendar('#calendar', {
            defaultView: 'week',
            useCreationPopup: false,
            useDetailPopup: useDetailPopup,
            calendars: CalendarList,
            taskView: false,
            disableDblClick: true,
            disableClick: true,
            isReadOnly:true,
            week: {
                daynames: [translations.sun, translations.mon,translations.tue,translations.wed,translations.thu,translations.fri,translations.sat],
            },
            scheduleView: ['time'],
            template: {
                time: function(schedule) {
                    return getTimeTemplate(schedule, false);
                },
                timegridDisplayPrimayTime: function(time) {
                    var hour = time.hour;
                    return hour +':00';
                }
            }
        });
    }

    // event handlers
    cal.on({
        'clickMore': function(e) {
            console.log('clickMore', e);
        },
        'clickSchedule': function(e) {
            if(calendarType==0) {
                // document.getElementsByClassName("tui-full-calendar-timegrid-schedules")[0].children[0].children[0].innerHTML += '<div style="position:absolute;width: 14.2857%; left: 0%; border-right: 1px solid rgb(229, 229, 229); background-color: rgba(81, 92, 230, 0.05);"> </div>';
                // );
                let index = getIndex(e.schedule.id);
                if (index != calendarUpdate.stepIndex && index != -1 && index != undefined) {
                    calendarUpdate.stepIndex = getIndex(e.schedule.id);
                    newSchedule = null;
                    setSchedules();
                    setUI();
                }
                if(e.schedule.id==suggestion.id){
                    cal.deleteSchedule(newSchedule.id, newSchedule.calendarId);
                    newSchedule = suggestion;
                    newSchedule.bgColor = '#5cb85c';
                    newSchedule.dragBgColor = '#5cb85c';
                    newSchedule.borderColor = '#5cb85c';
                    newSchedule.body=translations.noproblems;
                    newSchedule.isReadOnly = false;
                    suggestion = null;

                    cal.updateSchedule(newSchedule.id, newSchedule.calendarId, newSchedule);
                    let possibleDevices =  checkOverlap(newSchedule,document.getElementById("allowOverlap").checked);
                    addDevices(possibleDevices);
                    if(possibleDevices.length==0){
                        newSchedule.bgColor = '#d9534f';
                        newSchedule.dragBgColor = '#d9534f';
                        newSchedule.borderColor = '#d9534f';
                        newSchedule.body = translations.noavailable;
                        cal.updateSchedule(newSchedule.id, newSchedule.calendarId,newSchedule);
                    }
                    refreshScheduleVisibility();
                }
            }
            console.log('clickSchedule', e);
        },
        'clickDayname': function(date) {
            console.log('clickDayname', date);
        },
        'beforeCreateSchedule': function(e) {
            if(calendarType==0) {

                let schedule = getCurrentSchedule()
                if (schedule) {

                    newSchedule = schedule;
                    if (e.start) {
                        newSchedule.start = e.start;
                    }
                    if (e.end) {
                        newSchedule.end = e.end;
                    }
                    var check = checkContinuity(calendarUpdate.stepIndex, schedule,false,false)
                    if (check.ok||$('#deviceTypeDropdown').children().length!=0) {
                        newSchedule.bgColor = '#5cb85c';
                        newSchedule.dragBgColor = '#5cb85c';
                        newSchedule.borderColor = '#5cb85c';
                        newSchedule.body = check.message;
                        e.bgColor = '#5cb85c';
                        e.dragBgColor = '#5cb85c';
                        e.borderColor = '#5cb85c';
                        e.body = check.message;
                    } else {
                        newSchedule.bgColor = '#d9534f';
                        newSchedule.dragBgColor = '#d9534f';
                        newSchedule.borderColor = '#d9534f';
                        newSchedule.body = check.message;
                        e.bgColor = '#d9534f';
                        e.dragBgColor = '#d9534f';
                        e.borderColor = '#d9534f';
                        e.body = check.message;
                    }
                    cal.updateSchedule(schedule.id, schedule.calendarId, e);
                } else {
                    if (newSchedule) {
                        var calendar = e.calendar || findCalendar(e.calendarId);
                        cal.deleteSchedule(newSchedule.id, calendar.id);
                    }
                    saveNewSchedule(e);
                }
                $("#help").text("you can drag and drop the calendar or drag again");
                let possibleDevices =  checkOverlap(newSchedule,document.getElementById("allowOverlap").checked);
                addDevices(possibleDevices);
                if(possibleDevices.length==0){
                    newSchedule.bgColor = '#d9534f';
                    newSchedule.dragBgColor = '#d9534f';
                    newSchedule.borderColor = '#d9534f';
                    newSchedule.body = translations.noavailable;
                    cal.updateSchedule(newSchedule.id, newSchedule.calendarId,newSchedule);
                }
                CheckAllCalendars();
                refreshScheduleVisibility();
            }
        },
        'beforeUpdateSchedule': function(e) {
            if(calendarType==0) {
                var schedule = e.schedule;
                var changes = e.changes;
                newSchedule = schedule;
                console.log('beforeUpdateSchedule', e);
                if (e.changes.start) {
                    newSchedule.start = e.changes.start;
                }
                if (e.changes.end) {
                    newSchedule.end = e.changes.end;
                }
                var check = checkContinuity(calendarUpdate.stepIndex, schedule,false,false)
                if (check.ok) {
                    newSchedule.bgColor = '#5cb85c';
                    newSchedule.dragBgColor = '#5cb85c';
                    newSchedule.borderColor = '#5cb85c';
                    newSchedule.body = check.message;
                    changes.bgColor = '#5cb85c';
                    changes.dragBgColor = '#5cb85c';
                    changes.borderColor = '#5cb85c';
                    changes.body = check.message;
                } else {
                    newSchedule.bgColor = '#d9534f';
                    newSchedule.dragBgColor = '#d9534f';
                    newSchedule.borderColor = '#d9534f';
                    newSchedule.body = check.message;
                    changes.bgColor = '#d9534f';
                    changes.dragBgColor = '#d9534f';
                    changes.borderColor = '#d9534f';
                    changes.body = check.message;
                }
                let possibleDevices =  checkOverlap(newSchedule,document.getElementById("allowOverlap").checked);
                addDevices(possibleDevices);
                if(possibleDevices.length==0){
                    newSchedule.bgColor = '#d9534f';
                    newSchedule.dragBgColor = '#d9534f';
                    newSchedule.borderColor = '#d9534f';
                    newSchedule.body = translations.noavailable;
                }
                filledInSteps[calendarUpdate] =newSchedule
                cal.updateSchedule(newSchedule.id, newSchedule.calendarId,newSchedule);
                refreshScheduleVisibility();
            }
        },
        'beforeDeleteSchedule': function(e) {
            if(calendarType==0) {
                console.log('beforeDeleteSchedule', e);
                cal.deleteSchedule(e.schedule.id, e.schedule.calendarId);
            }
        },
        'afterRenderSchedule': function(e) {
            var schedule = e.schedule;
        },
        'clickTimezonesCollapseBtn': function(timezonesCollapsed) {
            console.log('timezonesCollapsed', timezonesCollapsed);
            if (timezonesCollapsed) {
                cal.setTheme({
                    'week.daygridLeft.width': '77px',
                    'week.timegridLeft.width': '77px'
                });
            } else {
                cal.setTheme({
                    'week.daygridLeft.width': '60px',
                    'week.timegridLeft.width': '60px'
                });
            }
            return true;
        }
    });
    /**
     * Get time template for time and all-day
     * @param {Schedule} schedule - schedule
     * @param {boolean} isAllDay - isAllDay or hasMultiDates
     * @returns {string}
     */
    function getTimeTemplate(schedule, isAllDay) {
        var html = [];
        var start = moment(schedule.start.toUTCString());
        if (!isAllDay) {
            html.push('<strong>' + start.format('HH:mm') + '</strong> ');
        }
        if (schedule.isPrivate) {
            html.push('<span class="calendar-font-icon ic-lock-b"></span>');
            html.push(' Private');
        } else {
            if (schedule.isReadOnly) {
                html.push('<span class="calendar-font-icon ic-readonly-b"></span>');
            } else if (schedule.recurrenceRule) {
                html.push('<span class="calendar-font-icon ic-repeat-b"></span>');
            } else if (schedule.attendees.length) {
                html.push('<span class="calendar-font-icon ic-user-b"></span>');
            } else if (schedule.location) {
                html.push('<span class="calendar-font-icon ic-location-b"></span>');
            }
            html.push(' ' + schedule.title);
        }

        return html.join('');
    }
    function CheckAllCalendars() {
        for(let current=0;current<filledInSteps.length;current++){
            if(filledInSteps[current]!=null){
                let continuity = checkContinuity(current,filledInSteps[current],document.getElementById("allowOverlap").checked,document.getElementById("withinOfficehoursSingleSuggest").checked,false);
                if(checkOverlap(filledInSteps[current],document.getElementById("allowOverlap").checked).length>0&&continuity.ok){
                    filledInSteps[current].bgColor = '#5cb85c';
                    filledInSteps[current].dragBgColor = '#5cb85c';
                    filledInSteps[current].borderColor = '#5cb85c';
                    filledInSteps[current].body=translations.noproblems;
                    cal.updateSchedule(filledInSteps[current].id, filledInSteps[current].calendarId, filledInSteps[current]);
                }else{
                    filledInSteps[current].bgColor = '#d9534f';
                    filledInSteps[current].dragBgColor = '#d9534f';
                    filledInSteps[current].borderColor = '#d9534f';
                    filledInSteps[current].body = continuity.message;
                    cal.updateSchedule(filledInSteps[current].id, filledInSteps[current].calendarId, filledInSteps[current]);
                }
            }
        }
    }
    /**
     * A listener for click the menu
     * @param {Event} e - click event
     */
    function onClickMenu(e) {
        var target = $(e.target).closest('a[role="menuitem"]')[0];
        var action = getDataAction(target);
        var options = cal.getOptions();
        var viewName = '';

        console.log(target);
        console.log(action);
        switch (action) {
            case 'toggle-daily':
                viewName = 'day';
                break;
            case 'toggle-weekly':
                viewName = 'week';
                break;
            case 'toggle-monthly':
                options.month.visibleWeeksCount = 0;
                viewName = 'month';
                break;
            case 'toggle-weeks2':
                options.month.visibleWeeksCount = 2;
                viewName = 'month';
                break;
            case 'toggle-weeks3':
                options.month.visibleWeeksCount = 3;
                viewName = 'month';
                break;
            case 'toggle-narrow-weekend':
                options.month.narrowWeekend = !options.month.narrowWeekend;
                options.week.narrowWeekend = !options.week.narrowWeekend;
                viewName = cal.getViewName();

                target.querySelector('input').checked = options.month.narrowWeekend;
                break;
            case 'toggle-start-day-1':
                options.month.startDayOfWeek = options.month.startDayOfWeek ? 0 : 1;
                options.week.startDayOfWeek = options.week.startDayOfWeek ? 0 : 1;
                viewName = cal.getViewName();

                target.querySelector('input').checked = options.month.startDayOfWeek;
                break;
            case 'toggle-workweek':
                options.month.workweek = !options.month.workweek;
                options.week.workweek = !options.week.workweek;
                viewName = cal.getViewName();

                target.querySelector('input').checked = !options.month.workweek;
                break;
            default:
                break;
        }

        cal.setOptions(options, true);
        cal.changeView(viewName, true);

        // setDropdownCalendarType();
        setRenderRangeText();
        setSchedules();
    }
    function saveScheduleChanges() {
        var str = newSchedule.start.getFullYear().toString()+ "-" + ("0" + (newSchedule.start.getMonth() + 1)).slice(-2) + "-" + ("0" + (newSchedule.start.getDate())).slice(-2) ;
        document.getElementById('startDate' + calendarUpdate.stepIndex + '').value = str;
        var str = newSchedule.end.getFullYear().toString()+ "-" + ("0" + (newSchedule.end.getMonth() + 1)).slice(-2) + "-" + ("0" + (newSchedule.end.getDate())).slice(-2) ;
        document.getElementById('endDate' + calendarUpdate.stepIndex + '').value =str;

        var str = ("0" + (newSchedule.start.getHours() )).slice(-2)+ ":" + ("0" + (newSchedule.start.getMinutes() )).slice(-2)  ;
        document.getElementById('startHour' + calendarUpdate.stepIndex + '').value =str;
        var str = ("0" + (newSchedule.end.getHours() )).slice(-2)+ ":" + ("0" + (newSchedule.end.getMinutes() )).slice(-2)  ;
        document.getElementById('endHour' + calendarUpdate.stepIndex + '').value = str;
        document.getElementById('selectDevice' + calendarUpdate.stepIndex + '').value =  document.getElementById('deviceTypeDropdown').value;

        // var check = checkContinuity(calendarUpdate.stepIndex,newSchedule,false,false);
        // if(check.ok) {
        //     document.getElementById('row' + calendarUpdate.stepIndex + '').setAttribute("style", "background-color:#5cb85c;");
        // }else{
        //     document.getElementById('row' + calendarUpdate.stepIndex + '').setAttribute("style", "background-color:#d9534f;");
        // }
        if(calendarUpdate.stepIndex<allExperiments[calendarUpdate.experimentIndex]['stepTypes'].length-1){

            if(preCalculatedSuggestions!=null&&preCalculatedSuggestions[calendarUpdate.stepIndex]!=null) {
                let startDate = preCalculatedSuggestions[calendarUpdate.stepIndex].start;
                let start = new Date(startDate.date.year, startDate.date.month - 1, startDate.date.day, startDate.time.hour, startDate.time.minute, 0, 0);
                let endDate = preCalculatedSuggestions[calendarUpdate.stepIndex].end;
                let end = new Date(endDate.date.year, endDate.date.month - 1, endDate.date.day, endDate.time.hour, endDate.time.minute, 0, 0);
                calendarUpdate.stepIndex++;
                if (preCalculatedSuggestions[calendarUpdate.stepIndex]!=null&&start.getTime() === newSchedule.start.getTime() && end.getTime() === newSchedule.end.getTime()) {
                    startDate = preCalculatedSuggestions[calendarUpdate.stepIndex].start;
                    start = new Date(startDate.date.year, startDate.date.month - 1, startDate.date.day, startDate.time.hour, startDate.time.minute, 0, 0);
                    endDate = preCalculatedSuggestions[calendarUpdate.stepIndex].end;
                    end = new Date(endDate.date.year, endDate.date.month - 1, endDate.date.day, endDate.time.hour, endDate.time.minute, 0, 0);
                    suggestion = createSuggestionSchedule(start, end, CalendarList[0])
                } else {
                    preCalculatedSuggestions = null;
                    suggestion = null;
                }
            }else {
                calendarUpdate.stepIndex++;
            }
            setSchedules();
            setUI();
            refreshScheduleVisibility();
        }
        else{
            $('#extraLargeModal').modal('toggle');
            $('.modal-backdrop').remove();
        }
    }
    function onClickNavi(e) {
        var action = getDataAction(e.target);

        switch (action) {
            case 'move-prev':
                cal.prev();
                break;
            case 'move-next':
                cal.next();
                break;
            case 'move-today':
                cal.today();
                break;
            default:
                return;
        }

        setRenderRangeText();
        setSchedules();
    }

    function onNewSchedule() {
        var title = $('#new-schedule-title').val();
        var location = $('#new-schedule-location').val();
        var isAllDay = document.getElementById('new-schedule-allday').checked;
        var start = datePicker.getStartDate();
        var end = datePicker.getEndDate();
        var calendar = selectedCalendar ? selectedCalendar : CalendarList[0];

        if (!title) {
            return;
        }

        cal.createSchedules([{
            id: String(chance.guid()),
            calendarId: calendar.id,
            title: title,
            isAllDay: isAllDay,
            start: start,
            end: end,
            category: isAllDay ? 'allday' : 'time',
            dueDateClass: '',
            color: calendar.color,
            stepIndex:-1,
            bgColor: calendar.bgColor,
            dragBgColor: calendar.bgColor,
            borderColor: calendar.borderColor,
            raw: {
                location: location
            },
            state: 'Busy'
        }]);

        $('#modal-new-schedule').modal('hide');
    }

    function onChangeNewScheduleCalendar(e) {
        var target = $(e.target).closest('a[role="menuitem"]')[0];
        var calendarId = getDataAction(target);
        changeNewScheduleCalendar(calendarId);
    }

    function changeNewScheduleCalendar(calendarId) {
        var calendarNameElement = document.getElementById('calendarName');
        var calendar = findCalendar(calendarId);
        var html = [];

        html.push('<span class="calendar-bar" style="background-color: ' + calendar.bgColor + '; border-color:' + calendar.borderColor + ';"></span>');
        html.push('<span class="calendar-name">' + calendar.name + '</span>');

        calendarNameElement.innerHTML = html.join('');

        selectedCalendar = calendar;
    }

    function createNewSchedule(event) {

        var start = event.start ? new Date(event.start.getTime()) : new Date();
        var end = event.end ? new Date(event.end.getTime()) : moment().add(1, 'hours').toDate();

        if (useCreationPopup) {
            cal.openCreationPopup({
                start: start,
                end: end
            });
        }
    }

    function saveNewSchedule(scheduleData) {
        var calendar = scheduleData.calendar || findCalendar(scheduleData.calendarId);
        var schedule = {
            id: String(chance.guid()),
            title: translations.step + (calendarUpdate.stepIndex+1)+': Experiment '+allExperiments[calendarUpdate.experimentIndex]['experimentTypeName']+', \n'+translations.device+' : '+allExperiments[calendarUpdate.experimentIndex]['stepTypes'][calendarUpdate.stepIndex]['deviceType']['deviceTypeName'],            start: scheduleData.start,
            end: scheduleData.end,
            category: 'time',
            dueDateClass: '',
            stepIndex:-1,
            color:calendar.color,
            bgColor: "#5cb85c",
            dragBgColor: "#5cb85c",
            borderColor: "#5cb85c",
            location: scheduleData.location,
            state: scheduleData.state
        };
        if (calendar) {
            schedule.calendarId = calendar.id;
            schedule.color = calendar.color;
            schedule.bgColor = "#5cb85c";
            schedule.borderColor = "#5cb85c";
        }
        var check = checkContinuity(calendarUpdate.stepIndex,schedule,false,false)
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
        newSchedule = schedule;
        cal.createSchedules([schedule]);

        refreshScheduleVisibility();
    }

    function onChangeCalendars(e) {
        var calendarId = e.target.value;
        var checked = e.target.checked;
        var viewAll = document.querySelector('.lnb-calendars-item input');
        var calendarElements = Array.prototype.slice.call(document.querySelectorAll('#calendarList input'));
        var allCheckedCalendars = true;

        if (calendarId === 'all') {
            allCheckedCalendars = checked;

            calendarElements.forEach(function(input) {
                var span = input.parentNode;
                input.checked = checked;
                span.style.backgroundColor = checked ? span.style.borderColor : 'transparent';
            });

            CalendarList.forEach(function(calendar) {
                calendar.checked = checked;
            });
        } else {
            findCalendar(calendarId).checked = checked;

            allCheckedCalendars = calendarElements.every(function(input) {
                return input.checked;
            });

            if (allCheckedCalendars) {
                viewAll.checked = true;
            } else {
                viewAll.checked = false;
            }
        }

        refreshScheduleVisibility();
    }

    function refreshScheduleVisibility() {
        var calendarElements = Array.prototype.slice.call(document.querySelectorAll('#calendarList input'));

        CalendarList.forEach(function(calendar) {
            cal.toggleSchedules(calendar.id, !calendar.checked, false);
        });

        cal.render(true);

        if(calendarType==0) {
            let parent = document.getElementsByClassName('tui-full-calendar-timegrid-schedules-container');
            var type = cal.getViewName();
            if (type === 'week') {
                for (let current = 0; current <= 6; current++) {
                    var startDate = new Date(cal.getDateRangeStart().getFullYear(), cal.getDateRangeStart().getMonth(), cal.getDateRangeStart().getDate(), 0,0);
                    startDate.setDate(startDate.getDate() + current);
                    let currentStartDate = new Date(startDate.getFullYear(), startDate.getMonth(), startDate.getDate(), startDate.getHours(), startDate.getMinutes());
                    let currentEndDate = new Date(startDate.getFullYear(), startDate.getMonth(), startDate.getDate(), startDate.getHours(), startDate.getMinutes());
                    currentEndDate.setMinutes(currentEndDate.getMinutes()+30);
                    for(let currentTimeslot=0;currentTimeslot<48;currentTimeslot++){
                        var schedule =  createSuggestionSchedule(currentStartDate,currentEndDate,CalendarList[0]);
                        if(!checkContinuity(calendarUpdate.stepIndex,schedule,false,true).ok||checkOverlap(schedule,document.getElementById("allowOverlap").checked).length==0){
                                var iDiv = document.createElement('div');
                                iDiv.id = 'greyout';
                                iDiv.className = 'greyout';
                                let percentage = 100 / 48 * currentTimeslot;
                                iDiv.style.cssText = "position:absolute;background-color: #d9534f;height:2.084%;top:" + percentage + "%;width:100%;opacity:0.25; ";
                                parent[0].children[current].prepend(iDiv);

                        }
                        currentStartDate.setMinutes(currentStartDate.getMinutes()+30);
                        currentEndDate.setMinutes(currentEndDate.getMinutes()+30);

                    }
                }
            }
        }
        calendarElements.forEach(function(input) {
            var span = input.nextElementSibling;
            span.style.backgroundColor = input.checked ? span.style.borderColor : 'transparent';
        });
    }

    function setDropdownCalendarType() {
        var calendarTypeName = document.getElementById('calendarTypeName');
        var calendarTypeIcon = document.getElementById('calendarTypeIcon');
        var options = cal.getOptions();
        var type = cal.getViewName();
        var iconClassName;

        if (type === 'day') {
            type = 'Daily';
            iconClassName = 'calendar-icon ic_view_day';
        } else if (type === 'week') {
            type = 'Weekly';
            iconClassName = 'calendar-icon ic_view_week';
        } else if (options.month.visibleWeeksCount === 2) {
            type = '2 weeks';
            iconClassName = 'calendar-icon ic_view_week';
        } else if (options.month.visibleWeeksCount === 3) {
            type = '3 weeks';
            iconClassName = 'calendar-icon ic_view_week';
        } else {
            type = 'Monthly';
            iconClassName = 'calendar-icon ic_view_month';
        }

        calendarTypeName.innerHTML = type;
        calendarTypeIcon.className = iconClassName;
    }

    function setRenderRangeText() {
        var renderRange = document.getElementById('renderRange');
        var options = cal.getOptions();
        var viewName = cal.getViewName();
        var html = [];
        if (viewName === 'day') {
            html.push(moment(cal.getDate().getTime()).format('DD.MM.YYYY'));
        } else if (viewName === 'month' &&
            (!options.month.visibleWeeksCount || options.month.visibleWeeksCount > 4)) {
            html.push(moment(cal.getDate().getTime()).format('MM.YYYY'));
        } else {
            html.push(moment(cal.getDateRangeStart().getTime()).format('DD.MM.YYYY'));
            html.push(' ~ ');
            html.push(moment(cal.getDateRangeEnd().getTime()).format(' DD.MM'));
        }
        renderRange.innerHTML = html.join('');
    }

    function setSchedules() {
        cal.clear();
        generateSchedule(cal.getViewName(), cal.getDateRangeStart(), cal.getDateRangeEnd());
        cal.createSchedules(ScheduleList);
        refreshScheduleVisibility();
    }

    function setEventListener() {
        $('#lnb-calendars').on('change', onChangeCalendars);
        $('#menu-navi').on('click', onClickNavi);
        $('.dropdown-menu a[role="menuitem"]').on('click', onClickMenu);
        $('#btn-save-schedule').on('click', onNewSchedule);
        $('#btn-new-schedule').on('click', createNewSchedule);
        $("#allowOverlap").on('click',function () {
            // calculateSuggestion(calendarUpdate.stepIndex,true,document.getElementById('withinOfficehoursSingleSuggest').checked);
            refreshScheduleVisibility()
        });
        $('#steps-items').on('click', changeviewmode);
        $('#dropdownMenu-calendars-list').on('click', onChangeNewScheduleCalendar);
        $("#extraLargeModal").on('show.bs.modal', setSchedules);
        $("#extraLargeModal").on('shown.bs.modal', setSchedules);
        $("#selectStep").on('click',saveScheduleChanges);
        $("#nextstep").on('click',nextStep);
        $("#previousstep").on('click',previousStep);
        $("#suggestStep").on('click',function () {
            // calculateSuggestion(calendarUpdate.stepIndex,true,document.getElementById('withinOfficehoursSingleSuggest').checked);
            getSuggestionFromServer(calendarUpdate.stepIndex,true,document.getElementById('withinOfficehoursSingleSuggest').checked);
        });
        window.addEventListener('resize', resizeThrottled);
    }
    function suggestionObject() {
        this.deviceType = null;
        this.start = null;
        this.end = null;
        this.continuity = null;
        this.fixedTimeHours = null
        this.fixedTimeMinutes=  null
        this.fixedTimeType = null
        this.hasFixedLength = false
    }
    function getSuggestionFromServer(stepIndex, personalAllowed,withinOfficeHours)
    {
        let steps = []
        for(let current = 0;current< allExperiments[calendarUpdate.experimentIndex]['stepTypes'].length;current++) {
            let newStep = new suggestionObject();
            // newStep.user
            if(filledInSteps[current]) {
                let start = filledInSteps[current]['start'];
                let end = filledInSteps[current]['end'];
                start = (new Date(start.getFullYear(),start.getMonth(),start.getDate(),start.getHours(),start.getMinutes(),0,0));
                end = (new Date(end.getFullYear(),end.getMonth(),end.getDate(),end.getHours(),end.getMinutes(),0,0))
                newStep.start = new Date(start.getTime() - start.getTimezoneOffset() * 60000).toISOString() ;
                newStep.end= new Date(end.getTime() - end.getTimezoneOffset() * 60000).toISOString() ;
            }
            newStep.deviceType = allExperiments[calendarUpdate.experimentIndex]['stepTypes'][current]['deviceType']
            newStep.continuity = allExperiments[calendarUpdate.experimentIndex]['stepTypes'][current]['continuity']
            newStep.hasFixedLength = allExperiments[calendarUpdate.experimentIndex]['stepTypes'][current]['hasFixedLength']
            newStep.fixedTimeHours = allExperiments[calendarUpdate.experimentIndex]['stepTypes'][current]['fixedTimeHours']
            newStep.fixedTimeMinutes = allExperiments[calendarUpdate.experimentIndex]['stepTypes'][current]['fixedTimeMinutes']
            newStep.fixedTimeType = allExperiments[calendarUpdate.experimentIndex]['stepTypes'][current]['fixedTimeType']
            delete newStep.deviceType.new ;
            delete newStep.continuity.new ;
            steps.push(newStep)
        }
        var date = new Date();
        date.setSeconds(0);
        date.setMinutes(date.getMinutes()/30);
        date.setMilliseconds(0);
        var suggestPost = {
             "steps": JSON.stringify(steps),
             "dateTime":date.toISOString(),
             "currentStep":calendarUpdate.stepIndex,
             "overlapAllowed":document.getElementById("allowOverlap").checked,
             "withinOfficeHours":document.getElementById("withinOfficehoursSingleSuggest").checked,
        }
        if(newSchedule){
            if(newSchedule.end){
                suggestPost.dateTime =new Date(newSchedule.end.getFullYear(), newSchedule.end.getMonth(), newSchedule.end.getDate(), newSchedule.end.getHours(), newSchedule.end.getMinutes(),0,0).toISOString();
            }
        }
        document.getElementById('suggestStep').disabled = true;

        $.ajax({
            type: "POST",
            contentType : 'application/json; charset=utf-8',
            dataType : 'json',
            accept: 'application/json',
            url: "calendar/calculateStepSuggestion",
            data: JSON.stringify(suggestPost), // Note it is important
            success :function(result) {
                if(result!=="failed") {
                    preCalculatedSuggestions = result;
                    let startDate = result[calendarUpdate.stepIndex].start;
                    let start = new Date(startDate.date.year, startDate.date.month - 1, startDate.date.day, startDate.time.hour, startDate.time.minute, 0, 0);
                    let endDate = result[calendarUpdate.stepIndex].end;
                    let end = new Date(endDate.date.year, endDate.date.month - 1, endDate.date.day, endDate.time.hour, endDate.time.minute, 0, 0);
                    suggestion = createSuggestionSchedule(start, end, CalendarList[0]);
                    cal.setDate(suggestion.start);
                    setSchedules();
                    refreshScheduleVisibility();
                    document.getElementById('suggestStep').disabled = false;
                    $('#toastsuggestionsuccess').toast('show')
                }else{
                    $('#toastsuggestionerror').toast('show')
                }
            },
            error: function(xhr, desc, err) {
                console.log(xhr);
                console.log("Details0: " + desc + "\nError:" + err);
            },
        });



    }

    function changeviewmode() {
        let viewmode = document.getElementById("steps-items").value;
        if(viewmode==="1"){
            showOwnItems=true;
            showOtherItems=false;
        }else if(viewmode==="2"){
            showOwnItems=false;
            showOtherItems=true;
        }else if(viewmode==="3"){
            showOwnItems=true;
            showOtherItems=true;
        }
        setSchedules();
        refreshScheduleVisibility()
    }
    function nextStep() {
        if(calendarUpdate.stepIndex<allExperiments[calendarUpdate.experimentIndex]['stepTypes'].length-1) {
            calendarUpdate.stepIndex++;
            setSchedules();
            setUI();
            var calendar = selectedCalendar ? selectedCalendar : CalendarList[0];
        }
        refreshScheduleVisibility();
    }
    function setUI(){

        if(calendarUpdate.stepIndex==allExperiments[calendarUpdate.experimentIndex].length-1){
            document.getElementById('selectStep').innerHTML = translations.finish;
        }
        document.getElementById('steptitle').innerText = allExperiments[calendarUpdate.experimentIndex]['stepTypes'][calendarUpdate.stepIndex]['deviceType']['deviceTypeName'];
        document.getElementById('length').innerText = translations.length+':'+allExperiments[calendarUpdate.experimentIndex]['stepTypes'][calendarUpdate.stepIndex]['fixedTimeHours']+"h"+allExperiments[calendarUpdate.experimentIndex]['stepTypes'][calendarUpdate.stepIndex]['fixedTimeMinutes'];
        if(calendarUpdate.stepIndex>0){

            if(allExperiments[calendarUpdate.experimentIndex]['stepTypes'][calendarUpdate.stepIndex-1]['continuity']['type']=="Hard"){
                document.getElementById('continuity2').innerText = translations.continuity+':'+translations.hard;
            }else if(allExperiments[calendarUpdate.experimentIndex]['stepTypes'][calendarUpdate.stepIndex-1]['continuity']['type']=="Soft (at most)"){
                document.getElementById('continuity2').innerText = translations.continuity+':'+translations.atmost;
            }else if(allExperiments[calendarUpdate.experimentIndex]['stepTypes'][calendarUpdate.stepIndex-1]['continuity']['type']=="Soft (at least)"){
                document.getElementById('continuity2').innerText = translations.continuity+':'+translations.atleast;
            }else{
                document.getElementById('continuity2').innerText = translations.continuity+':'+translations.none;
            }

            if(allExperiments[calendarUpdate.experimentIndex]['stepTypes'][calendarUpdate.stepIndex-1]['continuity']['directionType']=="After"){
                document.getElementById('align').innerText = translations.align+':'+translations.after;
            }else if(allExperiments[calendarUpdate.experimentIndex]['stepTypes'][calendarUpdate.stepIndex-1]['continuity']['directionType']=="Before"){
                document.getElementById('align').innerText = translations.align+':'+translations.before;
            }else{
                document.getElementById('align').innerText = translations.align+':'+translations.none;
            }
            document.getElementById('time').innerText = translations.time+':'+allExperiments[calendarUpdate.experimentIndex]['stepTypes'][calendarUpdate.stepIndex-1]['continuity']['hours']+"h "+allExperiments[calendarUpdate.experimentIndex]['stepTypes'][calendarUpdate.stepIndex-1]['continuity']['minutes']+"m";
        }else{
            document.getElementById('continuity2').innerText = translations.continuity+':'+translations.none;
            document.getElementById('align').innerText = translations.align+':'+translations.none;
            document.getElementById('time').innerText = translations.time+':'+translations.none;
        }
        calendarUpdate.start = document.getElementById('startDate' + calendarUpdate.stepIndex + '').value
        calendarUpdate.end = document.getElementById('endDate' + calendarUpdate.stepIndex + '').value
        calendarUpdate.startHour = document.getElementById('startHour' + calendarUpdate.stepIndex + '').value
        calendarUpdate.endHour = document.getElementById('endHour' + calendarUpdate.stepIndex + '').value
    }

    function previousStep() {
        if(calendarUpdate.stepIndex>0) {
            calendarUpdate.stepIndex--;
            setSchedules();
            setUI();
            var calendar = selectedCalendar ? selectedCalendar : CalendarList[0];
            // cal.deleteSchedule(newSchedule.id, calendar.id);
        }
        refreshScheduleVisibility();
    }

    function getDataAction(target) {
        return target.dataset ? target.dataset.action : target.getAttribute('data-action');
    }

    resizeThrottled = tui.util.throttle(function() {
        cal.render();
    }, 50);

    window.cal = cal;

    setRenderRangeText();
    setSchedules();
    setEventListener();
})(window, tui.Calendar);

// set calendars
(function() {
    if(calendarType==0){
        var calendarList = document.getElementById('legend');
        var html = [];
        CalendarList.forEach(function(calendar) {
            html.push('<div class="lnb-calendars-item" style="margin-left: 5px;"><label>' +
                '<input type="checkbox" class="tui-full-calendar-checkbox-round" value="' + calendar.id + '" checked>' +
                '<span style="border-color: ' + calendar.borderColor + '; background-color: ' + calendar.borderColor + ';"></span>' +
                '<span>' + calendar.name + '</span>' +
                '</label></div>'
            );
        });
        calendarList.innerHTML = html.join('\n');
    }else {
        var calendarList = document.getElementById('calendarList');
        var html = [];
        CalendarList.forEach(function (calendar) {
            html.push('<div class="lnb-calendars-item"><label>' +
                '<input type="checkbox" class="tui-full-calendar-checkbox-round" value="' + calendar.id + '" checked>' +
                '<span style="border-color: ' + calendar.borderColor + '; background-color: ' + calendar.borderColor + ';"></span>' +
                '<span>' + calendar.name + '</span>' +
                '</label></div>'
            );
        });
        calendarList.innerHTML = html.join('\n');
    }
})();
