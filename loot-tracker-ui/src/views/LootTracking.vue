<template>
  <div>
    <b-form v-on:submit.prevent="handleSubmit">
      <b-form-group
        id="reportKeyInputGroup"
        label-for="reportKeyInput"
        v-bind:invalid-feedback="invalidFeedback"
        v-bind:state="state"
      >
        <b-row>
          <b-col cols="7" offset-md="2">
            <b-form-input
              class="w-100 input-width"
              if="reportKeyInput"
              type="text"
              v-model="formData.key"
              v-bind:state="state"
              required
              trim
              placeholder="Enter api key"
            />
          </b-col>
          <b-col cols="1">
            <b-button v-bind:disabled="state === null || state === false" type="submit">Submit</b-button>
          </b-col>
        </b-row>
      </b-form-group>
    </b-form>

    <!-- row with on the left a small box with some player info: name, uni,  , on the right the total numbers plus pickable dates ratios -->
    <b-row>
      <b-col cols="6">
        <ul class="clean-list">
          <li>Username: {{ name }}</li>
          <li>Universe: {{ uni }}</li>
          <li>
             Date:
            <date-picker v-model="raidtimewindow" range lang="en" :shortcuts="shortcuts" confirm></date-picker>
          </li>
        </ul>
      </b-col>
      <b-col>
        <ul class="clean-list">
          <li>
            Metal:
            <vue-numeric separator="." v-model="totals.metal" read-only></vue-numeric>
          </li>
          <li>
            Crystal:
            <vue-numeric separator="." v-model="totals.crystal" read-only></vue-numeric>
          </li>
          <li>
            Deuterium:
            <vue-numeric separator="." v-model="totals.deuterium" read-only></vue-numeric>
          </li>
        </ul>
      </b-col>
    </b-row>

    <!-- table with all raids maybe paginate this late -->
    <b-row>
      <b-table striped hover :items="raids" :fields="fields"></b-table>
    </b-row>
  </div>
</template>

<script>
import { mapActions } from "vuex";
import VueNumeric from "vue-numeric";
import DatePicker from "vue2-datepicker";

export default {
  name: "lootTracking",
  components: {
    VueNumeric,
    DatePicker
  },
  data() {
    return {
      name: "henk",
      uni: "s159",
      raidtimewindow: [],
      formData: {
        key: ""
      },
      reportKeyRegex: RegExp(
        "((?:[a-z][a-z]+))(-)((?:[a-z][a-z]+))(-)([0-9]{1,3})(-)([a-f0-9]{40})"
      ),
      fields: [
        {
          key: "metal",
          sortable: true
        },
        {
          key: "crystal",
          sortable: false
        },
        {
          key: "deuterium",
          sortable: true
        },
        {
          key: "total",
          sortable: true
        }
      ],
      raids: [
        {
          metal: 1000,
          crystal: 1000,
          deuterium: 1000,
          total: 3000
        }
      ],
      totals: {
        metal: 1000000,
        crystal: 100000,
        deuterium: 10000,
        date: new Date("2017-01-26")
      },
      shortcuts: [
        {
          text: "Today",
          onClick: () => {
            this.raidtimewindow = [new Date(), new Date()];
          }
        },
        {
          text: "Past 7 days",
          onClick: () => {
            this.raidtimewindow = [
              new Date(Date.now() - 3600 * 1000 * 24 * 7),
              new Date()
            ];
          }
        },
        {
          text: "Past 30 days",
          onClick: () => {
            this.raidtimewindow = [
              new Date(Date.now() - 3600 * 1000 * 24 * 30),
              new Date()
            ];
          }
        }
      ]
    };
  },
  computed: {
    state: function() {
      if (this.formData.key === "") {
        return null;
      } else {
        return this.reportKeyRegex.test(this.formData.key);
      }
    },
    invalidFeedback: function() {
      return "Invalid key provided";
    }
  },
  methods: {
    ...mapActions(["addReport"]),
    handleSubmit: function() {
      console.log(this.formData.key);
      // Call action
      this.addReport(this.formData.key);
      this.formData.key = "";
    }
  }
};
</script>

<style>
.clean-list {
  list-style-type: none;
  text-align: left;
}
</style>
