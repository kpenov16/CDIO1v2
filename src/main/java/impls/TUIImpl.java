package impls;

import interfaces.Functionality;
import interfaces.Functionality.UserCreationException;
import interfaces.Functionality.UserDeletionException;
import interfaces.Functionality.UserNotFoundException;
import interfaces.Functionality.UserUpdateException;

import java.util.*;

public class TUIImpl {
    private Functionality func;

    public TUIImpl(Functionality func) {
        this.func = func;
    }

    private void showUserMenue() {
        System.out.format("To show the users in the database enter s/S: \n");
        System.out.format("To insert a user to the database enter i/I: \n");
        System.out.format("To update a user from the database enter u/U: \n");
        System.out.format("To delete a user from the database enter d/D: \n");
        System.out.format("To exit press q/Q: \n");
    }

    public void execute() {
        showUserMenue();
        Scanner in = new Scanner(System.in);
        String choice = in.nextLine();
        while( !choice.trim().equalsIgnoreCase("q") ){
            if(choice.trim().equalsIgnoreCase("s")){
                System.out.format(func.getFormatedUsers() + "\n");
            }else if(choice.trim().equalsIgnoreCase("i")){
                System.out.format("Enter User details below\n");
                System.out.format("Enter User Id here: ");
                String idStr = in.nextLine().trim();
                int id = -1;
                try {
                    id = Integer.parseInt(idStr);
                }catch (Exception e){
                    e.printStackTrace();
                }
                while (id <= 0){
                    System.out.format("%d was invalid id, try again!\n", id);
                    System.out.format("Enter User Id here: ");
                    idStr = in.nextLine().trim();
                    try {
                        id = Integer.parseInt(idStr);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                System.out.format("Enter User cpr here(ex.:0102886789): ");
                String cpr = in.nextLine().trim();
                while (!cpr.matches("[0-9]+") || cpr.length() != 10){
                    System.out.format("%s is incorrect.\n", cpr);
                    System.out.format("Enter User cpr here(ex.:0102886789): ");
                    cpr = in.nextLine().trim();
                }

                System.out.format("Enter User first name here: ");
                String firstName = in.nextLine().trim();

                System.out.format("Enter User initials here: ");
                String ini = in.nextLine().trim();
                while(!func.isAvailable(ini)){
                    System.out.format("ini: %s is in use, choose another one: ", ini);
                    ini = in.nextLine().trim();
                }
                System.out.format("Choose roles, for example: 1,3: \n");
                List<String> roles = func.getAvailableRoles();
                System.out.format("%s:\n",getRolesString(roles));
                String rolesStr = in.nextLine().trim();
                Set<String> newRoles = parseInput(roles, rolesStr, ",");

                try {
                    func.createUser(id, cpr, firstName, ini, new ArrayList<>(newRoles));
                    System.out.format("User details: id: %d, cpr: %s, user name: %s, ini: %s, roles: %s\n",
                            id, cpr, firstName, ini, newRoles.toString());
                    System.out.format("Password: %s\n",func.getPassword(id));
                    System.out.format("%s\n\n","Remember the password as it won't be shown again!");
                }catch (UserCreationException | UserNotFoundException e){
                    System.out.format("%s\n", e.getMessage());
                }
            }else if(choice.trim().equalsIgnoreCase("u")) {
                String idStr;
                int id = -1;
                boolean isUserInDB = false;
                String userDetails = "";
                do {
                    if (id <= 0 || !isUserInDB) {
                        System.out.format("Enter User Id here: ");
                        idStr = in.nextLine().trim();
                        try {
                            id = Integer.parseInt(idStr);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (id <= 0) {
                            System.out.format("%d was invalid id, try again!\n", id);
                        }
                    }
                    if (id > 0) {
                        try {
                            userDetails = func.startUserUpdate(id);
                            isUserInDB = true;
                        } catch (UserNotFoundException e) {
                            System.out.format("%s\n", e.getMessage());
                            System.out.format("The id %d was not found in the database, try again!\n", id);
                        }
                    }
                } while (id <= 0 || !isUserInDB);

                String updateChoice = "";
                String updatedUserDetails = "";
                while (!updateChoice.equalsIgnoreCase("q")) {
                    showUpdateUserMenue();
                    System.out.format("\nUser status: %s\n", userDetails);
                    updateChoice = in.nextLine().trim();
                    if (updateChoice.equalsIgnoreCase("n")) {
                        System.out.format("Enter new User first name here: ");
                        String firstName = in.nextLine().trim();
                        func.updateUserName(firstName);
                    }else if (updateChoice.equalsIgnoreCase("p")) {
                        System.out.format("Enter current password here: ");
                        String currentPass = in.nextLine().trim();
                        while (!func.isUserPasswordMatching(currentPass)){
                            System.out.format("Wrong current password typed, try again.\n");
                        System.out.format("Enter current password here: ");
                        currentPass = in.nextLine().trim();
                        }
                        System.out.format("Password rules: \n");
                        System.out.format("Adgangskoden skal indeholde mindst 6 og højst 50 tegn\n");
                        System.out.format("af mindst tre af de følgende fire kategorier:\n");
                        System.out.format("- små bogstaver (’a’ til ’z’),\n- store bogstaver (’A’ til ’Z’),\n");
                        System.out.format("- cifre (’0’ til ’9’),\n- specialtegn: '.', '-', '_', '+', '!', '?', '='\n\n");

                        System.out.format("Enter new password here: ");
                        String newPass = in.nextLine().trim();
                        while (!func.isPasswordValid(newPass)) {
                            System.out.format("Wrong new password format typed, try again.\n");
                            System.out.format("Enter new password here: ");
                            newPass = in.nextLine().trim();
                        }
                        func.updateUserPassword(newPass);
                    }else if (updateChoice.equalsIgnoreCase("r")) {
                        List<String> roles = func.getAvailableRoles();
                        System.out.format("Choose roles, for example: 1,5,2: \n");
                        String s = getRolesString(roles);
                        System.out.format("%s:\n", s);
                        String rolesStr = in.nextLine().trim();
                        func.setUserRolles(new ArrayList<>( parseInput(roles, rolesStr, ",") ));
                    }else if (updateChoice.equalsIgnoreCase("u")) {
                        try {
                            updatedUserDetails = func.applyUserUpdate();
                            updateChoice = "q";
                            System.out.format("Updated user: %s !\n", updatedUserDetails);
                        } catch (UserUpdateException e) {
                            System.out.format("%s\n", e.getMessage());
                        } finally {
                            func.clearUserUnderUpdate();
                        }
                    }else
                        System.out.format("%s was not an option, try again!\n", updateChoice.trim());
                }
            }else if(choice.trim().equalsIgnoreCase("d")){
                System.out.format("Enter id for User to be deleted from the database: ");
                int idToBeDeleted = -1;
                String input = "";
                try{
                    input = in.nextLine().trim();
                    idToBeDeleted = Integer.parseInt(input.trim());
                }catch (Exception e){
                    System.out.format("%s was not an integer, try again!\n", input);
                }
                if(idToBeDeleted < 1)
                    System.out.format("%d was invalid id, try again!\n", idToBeDeleted);
                else{
                    try {
                        func.deleteUser(idToBeDeleted);
                    } catch (UserDeletionException e) {
                        System.out.format("%s\n", e.getMessage());
                    }
                }
            }else
                System.out.format("%s was not an option, try again!\n", choice.trim());
            showUserMenue();
            choice = in.nextLine();
        }
        System.out.format("You've pressed q/Q. Goodbye!\n");
        in.close();
    }

    private Set<String> parseInput(List<String> roles, String rolesStr, String separator) {
        List<String> indexesStr = new ArrayList<>( Arrays.asList(rolesStr.split(separator)));
        Set<String> newRoles = new TreeSet<>();
        for (String index : indexesStr) {
            try {
                newRoles.add(roles.get(Integer.parseInt(index)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return newRoles;
    }

    private String getRolesString(List<String> roles) {
        String s = "";
        for (int i = 0; i < roles.size(); i++) {
            s += i + " " + roles.get(i) + ", ";
        }
        s = s.substring(0, s.length() - ", ".length());
        return s;
    }

    private void showUpdateUserMenue() {
        System.out.format("Update User first name, n/N\n");
        System.out.format("Update User password, p/P\n");
        System.out.format("Update User roles, r/R\n");
        System.out.format("Quit and update the User, u/U\n");
        System.out.format("Quit without updating the User, q/Q\n");
    }
}
