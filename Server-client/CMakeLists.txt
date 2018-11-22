cmake_minimum_required(VERSION 3.12)
project(PruebaClient C)

set(CMAKE_C_STANDARD 99)
find_package( Threads )
add_executable(PruebaClient Cliente.c Socket_Cliente.c Socket_Cliente.h Socket.h Socket.c Socket_Servidor.c Socket_Servidor.h)
target_link_libraries(PruebaClient ${CMAKE_THREAD_LIBS_INIT} )