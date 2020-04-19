'use strict';

/* eslint-disable require-jsdoc */
/* eslint-env jquery */
/* global moment, tui, chance */
/* global findCalendar, CalendarList, ScheduleList, generateSchedule */
let newSchedule;
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
                startDayOfWeek: 1 // monday
            },
            scheduleView: ['time'],
            template: {
                time: function(schedule) {
                    return getTimeTemplate(schedule, false);
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
                startDayOfWeek: 1 // monday
            },
            scheduleView: ['time'],
            template: {
                time: function(schedule) {
                    return getTimeTemplate(schedule, false);
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
                    var check = checkContinuity(calendarUpdate.stepIndex, schedule)
                    if (check.ok) {
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
                    refreshScheduleVisibility();
                } else {
                    if (newSchedule) {
                        var calendar = e.calendar || findCalendar(e.calendarId);
                        cal.deleteSchedule(newSchedule.id, calendar.id);
                    }
                    saveNewSchedule(e);

                }
                $("#help").text("you can drag and drop the calendar or drag again");

                checkOverlap();


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
                var check = checkContinuity(calendarUpdate.stepIndex, schedule)
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
                cal.updateSchedule(schedule.id, schedule.calendarId, changes);
                checkOverlap();

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
            // var element = cal.getElement(schedule.id, schedule.calendarId);
            // console.log('afterRenderSchedule', element);
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

        setDropdownCalendarType();
        setRenderRangeText();
        setSchedules();
    }
    function saveScheduleChanges() {
        newSchedule.start.get
        var str = newSchedule.start.getFullYear().toString()+ "-" + ("0" + (newSchedule.start.getMonth() + 1)).slice(-2) + "-" + ("0" + (newSchedule.start.getDate())).slice(-2) ;
        document.getElementById('startDate' + calendarUpdate.stepIndex + '').value = str;
        var str = newSchedule.end.getFullYear().toString()+ "-" + ("0" + (newSchedule.end.getMonth() + 1)).slice(-2) + "-" + ("0" + (newSchedule.end.getDate())).slice(-2) ;
        document.getElementById('endDate' + calendarUpdate.stepIndex + '').value =str;

        var str = ("0" + (newSchedule.start.getHours() )).slice(-2)+ ":" + ("0" + (newSchedule.start.getMinutes() )).slice(-2)  ;
        document.getElementById('startHour' + calendarUpdate.stepIndex + '').value =str;
        var str = ("0" + (newSchedule.end.getHours() )).slice(-2)+ ":" + ("0" + (newSchedule.end.getMinutes() )).slice(-2)  ;
        document.getElementById('endHour' + calendarUpdate.stepIndex + '').value = str;


        document.getElementById('selectDevice' + calendarUpdate.stepIndex + '').value =  document.getElementById('deviceTypeDropdown').value;

        var check = checkContinuity(calendarUpdate.stepIndex,newSchedule);
        if(check.ok) {
            document.getElementById('row' + calendarUpdate.stepIndex + '').setAttribute("style", "background-color:#5cb85c;");
        }else{
            document.getElementById('row' + calendarUpdate.stepIndex + '').setAttribute("style", "background-color:#d9534f;");
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
            title: 'Step '+(calendarUpdate.stepIndex+1)+' of Experiment '+allExperiments[calendarUpdate.experimentIndex]['experimentTypeName']+', \nDevice = '+allExperiments[calendarUpdate.experimentIndex]['stepTypes'][calendarUpdate.stepIndex]['deviceType']['deviceTypeName'],
            start: scheduleData.start,
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
        var check = checkContinuity(calendarUpdate.stepIndex,schedule)
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
            html.push(moment(cal.getDate().getTime()).format('YYYY.MM.DD'));
        } else if (viewName === 'month' &&
            (!options.month.visibleWeeksCount || options.month.visibleWeeksCount > 4)) {
            html.push(moment(cal.getDate().getTime()).format('YYYY.MM'));
        } else {
            html.push(moment(cal.getDateRangeStart().getTime()).format('YYYY.MM.DD'));
            html.push(' ~ ');
            html.push(moment(cal.getDateRangeEnd().getTime()).format(' MM.DD'));
        }
        renderRange.innerHTML = html.join('');
    }

    function setSchedules() {
        cal.clear();
        generateSchedule(cal.getViewName(), cal.getDateRangeStart(), cal.getDateRangeEnd());
        cal.createSchedules(ScheduleList);
        // var schedules = [
        //     {id: 489273, title: 'Workout for 2019-04-05', isAllDay: false, start: '2018-02-01T11:30:00+09:00', end: '2018-02-01T12:00:00+09:00', goingDuration: 30, comingDuration: 30, color: '#ffffff', isVisible: true, bgColor: '#69BB2D', dragBgColor: '#69BB2D', borderColor: '#69BB2D', calendarId: 'logged-workout', category: 'time', dueDateClass: '', customStyle: 'cursor: default;', isPending: false, isFocused: false, isReadOnly: true, isPrivate: false, location: '', attendees: '', recurrenceRule: '', state: ''},
        //     // {id: 18073, title: 'completed with blocks', isAllDay: false, start: '2018-11-17T09:00:00+09:00', end: '2018-11-17T10:00:00+09:00', color: '#ffffff', isVisible: true, bgColor: '#54B8CC', dragBgColor: '#54B8CC', borderColor: '#54B8CC', calendarId: 'workout', category: 'time', dueDateClass: '', customStyle: '', isPending: false, isFocused: false, isReadOnly: false, isPrivate: false, location: '', attendees: '', recurrenceRule: '', state: ''}
        // ];
        // cal.createSchedules(schedules);
        refreshScheduleVisibility();
    }

    function setEventListener() {
        $('#lnb-calendars').on('change', onChangeCalendars);

        $('#menu-navi').on('click', onClickNavi);
        $('.dropdown-menu a[role="menuitem"]').on('click', onClickMenu);
        $('#btn-save-schedule').on('click', onNewSchedule);
        $('#btn-new-schedule').on('click', createNewSchedule);
        $('#dropdownMenu-calendars-list').on('click', onChangeNewScheduleCalendar);
        $("#extraLargeModal").on('show.bs.modal', setSchedules);
        $("#selectStep").on('click',saveScheduleChanges);
        window.addEventListener('resize', resizeThrottled);
    }

    function getDataAction(target) {
        return target.dataset ? target.dataset.action : target.getAttribute('data-action');
    }

    resizeThrottled = tui.util.throttle(function() {
        cal.render();
    }, 50);

    window.cal = cal;

    setDropdownCalendarType();
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
