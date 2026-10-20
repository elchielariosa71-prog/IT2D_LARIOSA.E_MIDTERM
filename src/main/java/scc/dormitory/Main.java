

package scc.dormitory;

import scc.dormitory.dao.ScenarioDao;
import scc.dormitory.ui.ScenarioMenu;

public class Main {

    public static void main(String[] args) {
        ScenarioDao dao = new ScenarioDao();

        ScenarioMenu menu = new ScenarioMenu(dao);

        menu.start();
    }
}


