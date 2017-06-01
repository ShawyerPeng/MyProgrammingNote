[Drawer 详解 ·Material Design Part 3](http://www.open-open.com/lib/view/open1479003828580.html)  
[[Android] 在Android5.0下的带有DrawerLayout的工程模板](http://www.jianshu.com/p/67ecb1d571de)  

```java
public boolean onNavigationItemSelected(MenuItem item) {
    // Handle navigation view item clicks here.
    int id = item.getItemId();

    if (id == R.id.nav_camera) {
        // Handle the camera action
        Toast.makeText(this, "click camera", Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(MainActivity.this,CameraActivity.class);
        startActivity(intent);
    } else if (id == R.id.nav_gallery) {

    } else if (id == R.id.nav_slideshow) {

    } else if (id == R.id.nav_manage) {

    } else if (id == R.id.nav_share) {

    } else if (id == R.id.nav_send) {

    }

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.START);
    return true;
}
```