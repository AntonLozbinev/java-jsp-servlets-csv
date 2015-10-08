package my.csvuploader.servlets;

import my.csvuploader.dao.ContactDao;
import my.csvuploader.dao.DaoException;
import my.csvuploader.dao.DaoFactory;
import my.csvuploader.dto.Contact;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@WebServlet("/handler")
@MultipartConfig
public class RequestHandlerServlet extends HttpServlet {

    private final String PAGES = "pages";
    private final String UPLOAD = "upload";
    private final String SORT = "sort";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        requestHandler(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        requestHandler(req, resp);
    }

    private void requestHandler(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        switch (action) {
            case UPLOAD:
                uploadData(req, resp);
                break;
            case PAGES:
                getPageWithContacts(req, resp);
                break;
            case SORT:
                sortData(req, resp);
        }
    }

    private synchronized void uploadData(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        resp.setContentType("text/html;charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        try(InputStream in = req.getPart("userFile").getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            PrintWriter out = resp.getWriter()) {
            String separator = req.getParameter("separator");
            String nextLine;
            ContactDao dao = null;
            try {
                dao = DaoFactory.getInstance().getContactDao();
                while ((nextLine = reader.readLine()) != null) {
                    String[] contPar = nextLine.replaceAll("\"", "").split(separator);
                    Contact contact = new Contact(contPar[0], contPar[1], contPar[2], contPar[3], contPar[4]);
                    dao.insertOrUpdate(contact);
                }
                out.println("<p align=\"center\">Database update successful</p>");
                out.println("<p align=\"center\"><a href=\"index.jsp\">На главную</a></p>");
            } finally {
                if (dao != null) {
                    dao.closeDao();
                }
            }
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }

    private void getPageWithContacts(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int page = 1;
        int recordsPerPage = 5;
        if(req.getParameter("page") != null)
            page = Integer.parseInt(req.getParameter("page"));
        if (req.getParameter("recPerPage") != null && !"".equals(req.getParameter("recPerPage"))) {
            recordsPerPage = Integer.parseInt(req.getParameter("recPerPage"));
            req.getSession().setAttribute("recPerPage", recordsPerPage);
        }
        try {
            ContactDao contactDao = null;
            List<Contact> contacts;
            int totalRecords;
            try {
                contactDao = DaoFactory.getInstance().getContactDao();
                contacts = contactDao.getAll((page - 1) * recordsPerPage, recordsPerPage);
                totalRecords = contactDao.getTotalRecords();
            } finally {
                if (contactDao != null) {
                    contactDao.closeDao();
                }
            }
            int numberOfPages = (int) Math.ceil(totalRecords * 1.0 / recordsPerPage);
            req.getSession().setAttribute("contacts", contacts);
            req.getSession().setAttribute("numberOfPages", numberOfPages);
            req.getSession().setAttribute("currentPage", page);
            req.getRequestDispatcher("pages/overview.jsp").forward(req, resp);
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }

    private void sortData(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Contact> contacts = (List<Contact>) req.getSession().getAttribute("contacts");
        String sortParam = req.getParameter("sortParam");
        Method method = null;
        Method[] methods = Contact.class.getDeclaredMethods();
        for (Method met : methods) {
            if (met.getName().contains(sortParam) && met.getName().startsWith("get")) {
                method = met;
            }
        }
        final Method finalMethod = method;
        Collections.sort(contacts, new Comparator<Contact>() {
            @Override
            public int compare(Contact o1, Contact o2) {
                String firstParam = "";
                String secondParam = "";
                try {
                    firstParam = finalMethod != null ? (String) finalMethod.invoke(o1) : "";
                    secondParam = finalMethod != null ? (String) finalMethod.invoke(o2) : "";
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                return firstParam.compareTo(secondParam);
            }
        });
        req.getSession().setAttribute("contacts", contacts);
        req.getRequestDispatcher("pages/overview.jsp").forward(req, resp);
    }
}
