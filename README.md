# Memer

### TEAM SCAR         
SUFEI (Team Lead) -- 13hours
 - Memer button images and logo were collected from SquareSpace logos. I made the layout for Mainactivity and Vanilla meme. Initially, I attempted to import code that can support auto adjusting the EditText font size when text is too long, but ran into the issue where deleting characters would not bring text size back to large. Instead, I used normal EditText but make the test size relative to image height. After Alvin and I combined our two layouts, we worked on aligning the image to fit the screen properly. I also made style sheets for buttons and edittexts.

CHARLYN --  Worked on chooser, save function and share function.~10hrs

ALVIN --                ~10 hrs
  - Worked mostly on creating the functional layout for "demotivational poster" and editing the layout/UI to fit; implementing the viewswitcher to shift between the two meme layouts; helped with code to save meme to gallery.

Reinard - Created a OnClickListener for a Button that generates a dialog box for the user to choose from the camera or gallery. Created a temporary storage space the ImageView. Created a way on how to transferred data from one Activity to another. Added a saveInstances method for any variable necessary. ~10 hrs.

### Tasks
* Handle vanilla meme format (toggle button?)
* Handle demotivational poster format (toggle button?)
* Intent for camera -> user take/retake -> jump back to app
* Intent for camera roll -> jump back to app
* save meme to camera roll
* share meme on social media (implicit intent - android does it)
* layout/style sheet
* savedinstancestate

### Views
* Home Screen - Do you wanna use camera or camera roll? 
* Choose which meme you want (toggle?)
* Text editing view 
* Share on social media

### Permissions
* write external storage [android.permission.WRITE_EXTERNAL_STORAGE]
* read external storage [android.permission.READ_EXTERNAL_STORAGE]
* camera [android.permission.CAMERA]
