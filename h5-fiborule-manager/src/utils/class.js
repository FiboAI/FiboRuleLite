export default class {
    constructor(obj) {
        this.__origin = JSON.parse(JSON.stringify(obj))




        this.reSet = function () {
            let originKeys = Object.keys(this.__origin)
            originKeys.push(...['__origin','reSet','set'])
            for (var key in this) {
                if (originKeys.indexOf(key)==-1) {
                    delete this[key];
                }
            }
            Object.keys(this.__origin).forEach(value => {
                this[value] = this.__origin[value]
            })
        }
        this.reSet()


        this.set = function (obj) {
            Object.keys(obj).forEach(value => {
                this[value] = obj[value]
            })
        }
    }
}