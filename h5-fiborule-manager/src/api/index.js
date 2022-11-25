import request from '../utils/request';

export const fetchData = (params) => request.get('./table.json',{params})