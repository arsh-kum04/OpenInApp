**Folder Name**: com.example.openinapp
**File Name**: MainActivity.java

```java
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;

class MainActivity : AppCompatActivity() {
    //Variable to hold the reference to the NavController
    private lateinit var navController: NavController
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Setting the view for the activity
        setContentView(R.layout.activity_main);
        //Variable to hold the reference of the BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        //Setting Background of the bottomNavigationView to null
        bottomNavigationView.setBackground(null);
        //Disable the third item of the BottomNavigationView menu
        bottomNavigationView.getMenu().getItem(2).setEnabled(false);
        //Getting the NavHostFragment
        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        //Getting the NavController from the NavHostFragment
        navController = navHostFragment.getNavController();
        //Setting the graph to the NavController
        navController.setGraph(R.navigation.navigation_fragment);
        //Setting the BottomNavigationView with the NavController
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }
}
```