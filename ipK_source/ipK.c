/*
 * CopyRight (c) 2000-2001 ipKangaroo
 *
 */

/*
This is the .exe file...  This'll probably compile for any platform.
*/

#include <stdio.h>
#include <process.h>

//Executable file for ipK
main()
{
   printf("\n");
   printf("\n");
   printf("***********************\n");
   printf("*  ipK by ipKangaroo  *\n");
   printf("***********************\n");
   printf("\n");
   printf("\n");
   printf("Loading, please wait...\n");
   printf("\n");
   printf("\n");
   printf("DO NOT CLOSE THIS WINDOW  -->  PROGRAM WILL EXIT\n");
   printf("\n");
   printf("\n");
   printf(":)\n");
   execlp("java", " ipK", 0);   /* run the command */
        exit(0);
}
