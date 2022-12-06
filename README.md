# IntelliJ_Java_FileFinderProject
File search programm (in progress) (windows)

Intention: creating a fast and smart file search programm that can handel big collections of folder/file sections for servers or high number of drivers on a computer.
Already way faster than the normal Windows Explorer.

Added improvements:
 - enter InputField -> searching for file on all Drivers, list the files
 - enter PathInput -> searching for all files withing the path
 - onclick on row(file) inside the JTable opens the file with relevant programm
 - Add button -> opens Frame -> displays selected file inside the JTable
 - Help button shows how everything is working (Fields / Buttons)

Coming improvements:
 - search List -> searches for all written files
 - auto-detect drivers -> selected Driver -> searching only on selected driver
 - Database
 - ?


What I learned:
 - How to connect variables with each other
 - How to send and get parameters
 - How to connect JFrames and JModuls with Methods
 - How to use Threads and Runnable
 - How to search in drivers for specific files
 - How to send open Commands to different fileTypes and the relevant programm to open it (doc = Word, pdf = adobe, etc...)
 - How to if and else, while, switches, for swell as try and catch operations
 - Writer, Reader, chooser, select operations, keys, Values and much more...


What I'm about to learn:
 - Methods to auto-detect drivers
 - Import data from Excel to a JTable and revers
 - Improving myself


Example:

try {
    if (parameter < constant) {
        do something
        BufferedReader br = new BufferedReader(.....);
        ....etc...
    } else {
        do something else
    }
} catch (IOExecution e) {
   e.printTreeStack;
}

inside Thread
BufferedWriter bw = new BufferedWriter(...);
br.....

