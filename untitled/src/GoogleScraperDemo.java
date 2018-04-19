import com.jaunt.*;

//Jaunt demo: searches for 'butterflies' at Google and prints urls of search results from first page.

public class GoogleScraperDemo{

    public static String getSearchString(String titelName, String instituteName, String school)
    {
        StringBuilder res = new StringBuilder();
        res.append(titelName).append(" of ").append(instituteName).append(" in ").append(school);
        return res.toString();
    }

    public static void main(String[] args) throws JauntException{


        UserAgent userAgent = new UserAgent();
        userAgent.settings.autoSaveAsHTML = true;
        huaAgent huaagent = new huaAgent();
        huaagent.getFirtLink("university list in united states");
        huaagent.parseFirstLink();
        huaagent.getSchoolList();
        huaagent.searchall();
        //userAgent.visit("https://www.4icu.org/cn/chinese-universities.htm");       //visit google
        //System.out.println(userAgent.doc.innerHTML());

    }

}
