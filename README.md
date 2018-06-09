On linux(ubuntu, etc.) before starting the game, type the following in terminal:

export _JAVA_OPTIONS="-Dquantum.multithreaded=false"

Without it, the gameloop, which is an actiontimer here, won't block at 60fps and the snake will move(render) too fast.

Run with:  
  
 java -jar Snake.jar 
