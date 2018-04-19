/**
 * import for use jaunt
 */

import com.jaunt.*;


import java.util.ArrayList;
import java.util.List;

/**
 * creat my special agent extends juant
 */
public class huaAgent extends UserAgent
{
    /**
     * firstlink is the goole seach first link, we can use getFirstLink() function to get it, also can assign it directly
     * schoolAgent is used to search special key words in google
     * schoolist is the all school in the United States
     * titelName and instiuteName are all key words
     */
    private String firstLink;
    private UserAgent schoolAgent = new UserAgent();
    private List<String> schoolist= new ArrayList<>();
    public String[] titelName = {"Dean", "Department Chair", "Chancellor"};
    public String[] instituteName = {"Structural Engineering", "Mechanical Engineering", "Design Engineering"};

    /**
     * search the string
     * @param searched is the string you need to search by google.com
     *                 when we can get url, assign the url to the firstLink
     */

    public void getFirtLink (String searched)
    {
        try {
            this.visit("http://google.com");       //visit google
            this.doc.apply(searched);
            Document submit = this.doc.submit();
          //  Elements links = this.doc.findEvery("<h3 class=r>").findEvery("<a>");   //find search result links
           // for(Element link : links) System.out.println(link.getAt("href"));            //print results
          //  for(Element link : links) System.out.println(link.innerText() + "****" + link.toString());//("href") + "****" + link.innerHTML());            //print results
           // System.out.println(this.doc.innerHTML());
        } catch (ResponseException e) {
            e.printStackTrace();
        }
        catch (JauntException e) {
            e.printStackTrace();
        }
    }



    /**
     * parse the firstlink to get the link of all university list
     */
    public void parseFirstLink()
    {
        Elements links = this.doc.findEvery("<h3 class=r>").findEvery("<a>");   //find search result links
        // for(Element link : links) System.out.println(link.getAt("href"));            //print results
        for(Element link : links) {

                System.out.println(link.innerText() + "****" + link.outerHTML());//("http"));//("href") + "****" + link.innerHTML());            //print results
                if (link.innerText().toLowerCase().contains("list"))
                {
                    this.firstLink = this.getURL(link.outerHTML());
                    System.out.println(this.firstLink);
                    return ;
                }
        }


    }

    /**
     * get a String include the url, but the url combinate with google url, so cut the url then return it
     * @param wholeLink the whole string
     * @return the url
     */

    public String getURL(String wholeLink)
    {
        StringBuilder res = new StringBuilder(wholeLink);
        int a = res.indexOf("url");
        int b = res.indexOf("&");
        return res.substring(a + 6, b);
    }

    /**
     * get all school name in United States
     */
    public void getSchoolList()
    {
        try {
            this.visit(this.firstLink);       //visit google

            Elements links = this.doc.findEvery("<ul>").findEvery("<a");   //find search result links
           //  for(Element link : links) System.out.println(link.getAt("href"));            //print results
          for(Element link : links)
          {
             // System.out.println(link.innerText() + "****" + link.toString());//("href") + "****" + link.innerHTML();// print result
              String tmp = link.innerText().toLowerCase();
              String[] tmparr = tmp.split("\n");
              for (int i = 0; i < tmparr.length; i++) {
                  if ((tmparr[i].contains("university") || tmp.contains("college"))
                          && !tmparr[i].contains("list") && !tmparr[i].contains("system")){
                      if (tmparr[i].contains("(")) {
                          int a = tmparr[i].indexOf("(");
                          tmparr[i] = tmparr[i].substring(0, a);
                      }
                      if (100 > tmparr[i].length()) {
                          this.schoolist.add(tmparr[i]);
                          System.out.println(schoolist.size() +":" + tmparr[i] + "-------");
                      }
                  }
              }
          }
          //  System.out.println(this.doc.innerHTML());
        } catch (ResponseException e) {
            e.printStackTrace();
        }
        catch (JauntException e) {
            e.printStackTrace();
        }
    }

    /**
     * combinate the four world to an string
     * @param titelName
     * @param instituteName
     * @param school
     * @return
     */
    public  String getSearchString(String titelName, String instituteName, String school)
    {
        StringBuilder res = new StringBuilder();
        res.append(titelName).append(" of ").append(instituteName).append(" in ").append(school);
        return res.toString();
    }

    /**
     * search all schools
     */
    public void searchall()
    {

        for(int index = 0; index < schoolist.size(); index++)
        {
            System.out.println("No." + index + "school ----------" + schoolist.get(index) + "----------------");
            searchConb(schoolist.get(index));

        }
    }

    /**
     * check if all the key words in the context
     * @param s
     * @param titelName
     * @param institudeName
     * @return
     */

    public boolean checkContent(String s, String titelName, String institudeName){
        try {
            schoolAgent.visit(s);
            if (schoolAgent.doc.innerHTML().contains(titelName) && schoolAgent.doc.innerHTML().contains(institudeName))
            {
                System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                System.out.println("the url of the search result is ：" + s + "!!!!!!!!");
                System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n");


                //System.out.println(schoolAgent.doc.innerHTML());
                return true;
            }
        } catch (ResponseException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * search all combination of all key words
     * @param school
     */
    public void searchConb(String school) {
        List<String> allLink = new ArrayList();

        for (int i = 0; i < titelName.length; i++) {
            for (int j = 0; j < instituteName.length; j++) {
                String tmp = getSearchString(titelName[i], instituteName[j], school);
               // System.out.println("-------------------------------------");
                System.out.println("the key words that i searched is: (" + tmp + ")");
               // System.out.println("-------------------------------------" + tmp);
                try {
                    this.visit("http://google.com");       //visit google
                    this.doc.apply(tmp);
                    this.doc.submit();
                } catch (ResponseException | SearchException e) {
                    e.printStackTrace();
                } catch (JauntException e) {
                    e.printStackTrace();
                }
                Elements links = this.doc.findEvery("<h3 class=r>").findEvery("<a>");
                int m = 0;
                for (Element link : links) {
                    String tmp_str = this.getURL(link.outerHTML());
                    if (!allLink.contains(tmp_str)) {
                        allLink.add(tmp_str);
                        if (checkContent(tmp_str, titelName[i], instituteName[j]))
                           // System.exit(1);
                            return ;

                    }
                    if (m > 2)
                        break;
                    m++;
                }
                System.out.println("nothing matched!");
            }
        }
    }

    //public void getFulltext()
}
