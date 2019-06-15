<template>
  <div id="app">
    <div id="nav">
      <router-link to="/">Home</router-link> |
      <router-link to="/loot">Loot tracking</router-link> |
      <router-link to="/activity">Activity tracking</router-link>
    </div>
    <b-container>
      <router-view />
    </b-container>
  </div>
</template>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #FEFFFF;
  background-color: #3AAFA9;
  background-size: cover;
  height: 100vh;  
}
#nav {
  padding: 30px;
}

#nav a {
  font-weight: bold;
  color: #FEFFFF;
}

#nav a.router-link-exact-active {
  color: #17252A;
}
</style>
