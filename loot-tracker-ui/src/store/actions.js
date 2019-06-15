export default {
  addReport: function({ commit }, reportKey) {
    fetch('http://localhost:8585/ogame-tracker/track/loot/combat', {
      method: 'post',
      body: JSON.stringify({ reportKey }),
      headers: { 'Content-Type': 'application/json', 'Origin': '*' }
    }).then((response) => {
      console.log('---- Response recieved ----')
      console.log(response)
      commit('addReport', response)
    })
  }
}
