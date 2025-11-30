package controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.AppointmentDAO;
import dao.AvailabilityDAO;
import model.Appointment;
import model.Availability;

@WebServlet("/barber/schedule")
public class BarberScheduleServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private AppointmentDAO appointmentDAO;
    private AvailabilityDAO availabilityDAO;

    @Override
    public void init() throws ServletException {
        appointmentDAO = new AppointmentDAO();
        availabilityDAO = new AvailabilityDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || !"barber".equals(session.getAttribute("userType"))) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int barberId = (Integer) session.getAttribute("userId");

        String weekParam = request.getParameter("week");
        LocalDate startOfWeek;

        if (weekParam != null && !weekParam.isEmpty()) {
            startOfWeek = LocalDate.parse(weekParam);
        } else {
            startOfWeek = LocalDate.now().with(WeekFields.of(Locale.FRANCE).dayOfWeek(), 1);
        }

        List<LocalDate> weekDays = new ArrayList<>();
        List<Date> weekDaysAsDate = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            LocalDate day = startOfWeek.plusDays(i);
            weekDays.add(day);
            weekDaysAsDate.add(Date.from(day.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        }

        Map<LocalDate, List<Appointment>> appointmentsByDay = new HashMap<>();
        for (LocalDate date : weekDays) {
            List<Appointment> dayAppointments = appointmentDAO.findByBarberAndDate(barberId, date);
            appointmentsByDay.put(date, dayAppointments);
        }

        LocalDate previousWeek = startOfWeek.minusWeeks(1);
        LocalDate nextWeek = startOfWeek.plusWeeks(1);

        Date todayDate = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());

        List<Availability> availability = availabilityDAO.findByBarberId(barberId);

        request.setAttribute("weekDays", weekDays);
        request.setAttribute("weekDaysAsDate", weekDaysAsDate);
        request.setAttribute("todayDate", Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));

        request.setAttribute("appointmentsByDay", appointmentsByDay);
        request.setAttribute("previousWeek", previousWeek);
        request.setAttribute("nextWeek", nextWeek);
        request.setAttribute("currentWeekStart", startOfWeek);
        request.setAttribute("availability", availability);

        request.getRequestDispatcher("/WEB-INF/views/barber/schedule.jsp").forward(request, response);
    }
}
