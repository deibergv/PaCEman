# CMAKE generated file: DO NOT EDIT!
# Generated by "Unix Makefiles" Generator, CMake Version 3.12

# Delete rule output on recipe failure.
.DELETE_ON_ERROR:


#=============================================================================
# Special targets provided by cmake.

# Disable implicit rules so canonical targets will work.
.SUFFIXES:


# Remove some rules from gmake that .SUFFIXES does not remove.
SUFFIXES =

.SUFFIXES: .hpux_make_needs_suffix_list


# Suppress display of executed commands.
$(VERBOSE).SILENT:


# A target that is always out of date.
cmake_force:

.PHONY : cmake_force

#=============================================================================
# Set environment variables for the build.

# The shell in which to execute make rules.
SHELL = /bin/sh

# The CMake executable.
CMAKE_COMMAND = /snap/clion/49/bin/cmake/linux/bin/cmake

# The command to remove a file.
RM = /snap/clion/49/bin/cmake/linux/bin/cmake -E remove -f

# Escaping for special characters.
EQUALS = =

# The top-level source directory on which CMake was run.
CMAKE_SOURCE_DIR = /home/tony/Escritorio/PruebaClient

# The top-level build directory on which CMake was run.
CMAKE_BINARY_DIR = /home/tony/Escritorio/PruebaClient/cmake-build-debug

# Include any dependencies generated for this target.
include CMakeFiles/PruebaClient.dir/depend.make

# Include the progress variables for this target.
include CMakeFiles/PruebaClient.dir/progress.make

# Include the compile flags for this target's objects.
include CMakeFiles/PruebaClient.dir/flags.make

CMakeFiles/PruebaClient.dir/Cliente.c.o: CMakeFiles/PruebaClient.dir/flags.make
CMakeFiles/PruebaClient.dir/Cliente.c.o: ../Cliente.c
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=/home/tony/Escritorio/PruebaClient/cmake-build-debug/CMakeFiles --progress-num=$(CMAKE_PROGRESS_1) "Building C object CMakeFiles/PruebaClient.dir/Cliente.c.o"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -o CMakeFiles/PruebaClient.dir/Cliente.c.o   -c /home/tony/Escritorio/PruebaClient/Cliente.c

CMakeFiles/PruebaClient.dir/Cliente.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing C source to CMakeFiles/PruebaClient.dir/Cliente.c.i"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -E /home/tony/Escritorio/PruebaClient/Cliente.c > CMakeFiles/PruebaClient.dir/Cliente.c.i

CMakeFiles/PruebaClient.dir/Cliente.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling C source to assembly CMakeFiles/PruebaClient.dir/Cliente.c.s"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -S /home/tony/Escritorio/PruebaClient/Cliente.c -o CMakeFiles/PruebaClient.dir/Cliente.c.s

CMakeFiles/PruebaClient.dir/Socket_Cliente.c.o: CMakeFiles/PruebaClient.dir/flags.make
CMakeFiles/PruebaClient.dir/Socket_Cliente.c.o: ../Socket_Cliente.c
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=/home/tony/Escritorio/PruebaClient/cmake-build-debug/CMakeFiles --progress-num=$(CMAKE_PROGRESS_2) "Building C object CMakeFiles/PruebaClient.dir/Socket_Cliente.c.o"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -o CMakeFiles/PruebaClient.dir/Socket_Cliente.c.o   -c /home/tony/Escritorio/PruebaClient/Socket_Cliente.c

CMakeFiles/PruebaClient.dir/Socket_Cliente.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing C source to CMakeFiles/PruebaClient.dir/Socket_Cliente.c.i"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -E /home/tony/Escritorio/PruebaClient/Socket_Cliente.c > CMakeFiles/PruebaClient.dir/Socket_Cliente.c.i

CMakeFiles/PruebaClient.dir/Socket_Cliente.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling C source to assembly CMakeFiles/PruebaClient.dir/Socket_Cliente.c.s"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -S /home/tony/Escritorio/PruebaClient/Socket_Cliente.c -o CMakeFiles/PruebaClient.dir/Socket_Cliente.c.s

CMakeFiles/PruebaClient.dir/Socket.c.o: CMakeFiles/PruebaClient.dir/flags.make
CMakeFiles/PruebaClient.dir/Socket.c.o: ../Socket.c
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=/home/tony/Escritorio/PruebaClient/cmake-build-debug/CMakeFiles --progress-num=$(CMAKE_PROGRESS_3) "Building C object CMakeFiles/PruebaClient.dir/Socket.c.o"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -o CMakeFiles/PruebaClient.dir/Socket.c.o   -c /home/tony/Escritorio/PruebaClient/Socket.c

CMakeFiles/PruebaClient.dir/Socket.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing C source to CMakeFiles/PruebaClient.dir/Socket.c.i"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -E /home/tony/Escritorio/PruebaClient/Socket.c > CMakeFiles/PruebaClient.dir/Socket.c.i

CMakeFiles/PruebaClient.dir/Socket.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling C source to assembly CMakeFiles/PruebaClient.dir/Socket.c.s"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -S /home/tony/Escritorio/PruebaClient/Socket.c -o CMakeFiles/PruebaClient.dir/Socket.c.s

CMakeFiles/PruebaClient.dir/Socket_Servidor.c.o: CMakeFiles/PruebaClient.dir/flags.make
CMakeFiles/PruebaClient.dir/Socket_Servidor.c.o: ../Socket_Servidor.c
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=/home/tony/Escritorio/PruebaClient/cmake-build-debug/CMakeFiles --progress-num=$(CMAKE_PROGRESS_4) "Building C object CMakeFiles/PruebaClient.dir/Socket_Servidor.c.o"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -o CMakeFiles/PruebaClient.dir/Socket_Servidor.c.o   -c /home/tony/Escritorio/PruebaClient/Socket_Servidor.c

CMakeFiles/PruebaClient.dir/Socket_Servidor.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing C source to CMakeFiles/PruebaClient.dir/Socket_Servidor.c.i"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -E /home/tony/Escritorio/PruebaClient/Socket_Servidor.c > CMakeFiles/PruebaClient.dir/Socket_Servidor.c.i

CMakeFiles/PruebaClient.dir/Socket_Servidor.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling C source to assembly CMakeFiles/PruebaClient.dir/Socket_Servidor.c.s"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -S /home/tony/Escritorio/PruebaClient/Socket_Servidor.c -o CMakeFiles/PruebaClient.dir/Socket_Servidor.c.s

# Object files for target PruebaClient
PruebaClient_OBJECTS = \
"CMakeFiles/PruebaClient.dir/Cliente.c.o" \
"CMakeFiles/PruebaClient.dir/Socket_Cliente.c.o" \
"CMakeFiles/PruebaClient.dir/Socket.c.o" \
"CMakeFiles/PruebaClient.dir/Socket_Servidor.c.o"

# External object files for target PruebaClient
PruebaClient_EXTERNAL_OBJECTS =

PruebaClient: CMakeFiles/PruebaClient.dir/Cliente.c.o
PruebaClient: CMakeFiles/PruebaClient.dir/Socket_Cliente.c.o
PruebaClient: CMakeFiles/PruebaClient.dir/Socket.c.o
PruebaClient: CMakeFiles/PruebaClient.dir/Socket_Servidor.c.o
PruebaClient: CMakeFiles/PruebaClient.dir/build.make
PruebaClient: CMakeFiles/PruebaClient.dir/link.txt
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --bold --progress-dir=/home/tony/Escritorio/PruebaClient/cmake-build-debug/CMakeFiles --progress-num=$(CMAKE_PROGRESS_5) "Linking C executable PruebaClient"
	$(CMAKE_COMMAND) -E cmake_link_script CMakeFiles/PruebaClient.dir/link.txt --verbose=$(VERBOSE)

# Rule to build all files generated by this target.
CMakeFiles/PruebaClient.dir/build: PruebaClient

.PHONY : CMakeFiles/PruebaClient.dir/build

CMakeFiles/PruebaClient.dir/clean:
	$(CMAKE_COMMAND) -P CMakeFiles/PruebaClient.dir/cmake_clean.cmake
.PHONY : CMakeFiles/PruebaClient.dir/clean

CMakeFiles/PruebaClient.dir/depend:
	cd /home/tony/Escritorio/PruebaClient/cmake-build-debug && $(CMAKE_COMMAND) -E cmake_depends "Unix Makefiles" /home/tony/Escritorio/PruebaClient /home/tony/Escritorio/PruebaClient /home/tony/Escritorio/PruebaClient/cmake-build-debug /home/tony/Escritorio/PruebaClient/cmake-build-debug /home/tony/Escritorio/PruebaClient/cmake-build-debug/CMakeFiles/PruebaClient.dir/DependInfo.cmake --color=$(COLOR)
.PHONY : CMakeFiles/PruebaClient.dir/depend

