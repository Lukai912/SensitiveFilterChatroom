<template>
    <div>
        <el-row :gutter="20">
            <el-col :span="8">
                <el-card shadow="hover" class="mgb20" style="height:250px;">
                    <div class="user-info">
                        <img src="../assets/img/img.jpg" class="user-avator" alt />
                        <div class="user-info-cont">
                            <el-select v-model="selectValue" @change="selectOne" placeholder="请选择">
                                <el-option
                                v-for="item in rooms"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value">
                                </el-option>
                            </el-select>
                        </div>
                    </div>
                    <div class="user-info-list">
                        房间信息：
                        <span>{{selectValue}}</span>
                    </div>
                </el-card>
                <el-card shadow="hover" class="mgb20" style="height:200px;">
                        <div slot="header" class="clearfix">
                            <span>会话top主题</span>
                        </div>
                        <el-table :show-header="false" :data="top_topic" style="width:100%;">
                            <el-table-column width="40">
                            </el-table-column>
                            <el-table-column>
                                <template slot-scope="scope">
                                    <div>{{scope.row.title}}</div>
                                </template>
                            </el-table-column>
                        </el-table>
                    </el-card>
            </el-col>
            <el-col :span="16">
                <el-card shadow="hover" style="height:480px;">
                    <div slot="header" class="clearfix">
                        <span>聊天内容</span>
                        <el-select v-model="selectTopic" @change="selectOne" placeholder="请选择主题">
                                <el-option
                                v-for="item in topic_percentage"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value">
                                </el-option>
                        </el-select>
                    </div>
                    <el-table :show-header="false" :data="history_log" style="width:100%;">
                        <el-scrollbar>
                            <el-table-column>
                                <template slot-scope="scope">
                                   <div> {{scope.row.name+'：'+scope.row.content}}</div>
                                </template>
                            </el-table-column>
                        </el-scrollbar>
                    </el-table>
                </el-card>
            </el-col>
        </el-row>
        <el-row :gutter="20">
            <el-col :span="12">
                <el-card shadow="hover">
                    <div id="mypie" class="schart" canvasId="bar" :options="option" autoresize></div>
                </el-card>
            </el-col>
            <el-col :span="12">
                <el-card shadow="hover">
                    <div slot="header" class="clearfix">
                        <span>会话主题百分比</span>
                        <span class="user-info-cont">(提到该分类词的对话/总对话条数)</span>
                    </div>
                    <el-table :show-header="false" :data="topic_percentage" style="width:100%;">
                        <el-table-column>
                            <template slot-scope="scope">
                                {{scope.row.topic}}
                                <el-progress :percentage="scope.row.value" color="#42b983" @click="selectOne"></el-progress>
                            </template>
                        </el-table-column>
                    </el-table>
                    
                </el-card>
            </el-col>
        </el-row>
    </div>
</template>

<script>
import ECharts from 'echarts';
import bus from '../assets/bus';
import 'echarts/lib/chart/pie'; 
import axios from "axios";

export default {
    name: 'dashboard',
    data() {
        return {
            currentRoomInfo: {},
            selectValue: '',
            selectTopic:'',
            rooms: [],
            name: localStorage.getItem('ms_username'),
            top_topic:[],
            history_log:[],
            topic_percentage:[],
            todoList: [
                {
                    title: 'mmm:大家好，今天出来完王者荣耀么？',
                    percentage: 70
                },
                {
                    title: 'sss:不了，我要吃饭了',
                    percentage: 70
                },
                {
                    title: 'kkk:我不饿，今天还要加班',
                    percentage: 70
                },
                {
                    title: 'sss:最近加班有点累',
                    percentage: 70
                },
                {
                    title: 'mmm:啊，好吧',
                    percentage: 70
                },
                {
                    title: 'mmm:大家都去忙工作吧',
                    percentage: 70
                }
            ],
            option : {
                title: {
                    text: "敏感词类型分布"
                },
                tooltip: {
                    trigger: 'item',
                    formatter: '{a} <br/>{b}: {c} ({d}%)',
                    show:true
                },
                legend: {
                    orient: 'vertical',
                    left: 5,
                    data: ['直接访问', '邮件营销', '联盟广告', '视频广告', '搜索引擎']
                },
                series: [
                    {
                        name: '敏感词类型分布',
                        type: 'pie',
                        radius: ['50%', '70%'],
                        avoidLabelOverlap: false,
                        label: {
                            show: true,
                            position: 'outside'
                        },
                        emphasis: {
                            label: {
                                show: true,
                                fontSize: '30',
                                fontWeight: 'bold'
                            }
                        },
                        labelLine: {
                            show: true
                        },
                        data: [
                            {value: 335, name: '直接访问'},
                            {value: 310, name: '邮件营销'},
                            {value: 234, name: '联盟广告'},
                            {value: 135, name: '视频广告'},
                            {value: 1548, name: '搜索引擎'}
                        ]
                    }
                ]
            }
        };
    },
    components: {
        'v-chart': ECharts
    },
    computed: {
        role() {
            return this.name === 'admin' ? '超级管理员' : '普通房间c';
        }
    },
    mounted() {
        this.mypie = ECharts.init(document.getElementById('mypie'))
        // this.getHistoryLog()
        this.getHistoryRoom()
    },

    // created() {
    //     this.handleListener();
    //     this.changeDate();
    // },
    // activated() {
    //     this.handleListener();
    // },
    // deactivated() {
    //     window.removeEventListener('resize', this.renderChart);
    //     bus.$off('collapse', this.handleBus);
    // },
    methods: {
        selectOne(){
            this.getAnalysisData()
            this.getHistoryLog()
            this.getHistoryRoom()
        },
        changeDate() {
            const now = new Date().getTime();
            this.data.forEach((item, index) => {
                const date = new Date(now - (6 - index) * 86400000);
                item.name = `${date.getFullYear()}/${date.getMonth() + 1}/${date.getDate()}`;
            });
        },
        getHistoryRoom(){
           axios.get("http://localhost:8080/historyList").then((res) => {
                console.log(res.data)
                this.setRooms(res.data)
                console.log(this.rooms)
            }) 
        },
        setRooms(data){
            const room_option = []
            for(var i=0;i<data.length;i++){
                room_option.push({value: data[i], label: data[i]})
            }
            this.rooms = room_option
        },
        getAnalysisData(){
            axios.get("http://localhost:8080/analysis?log="+this.selectValue).then((res) => {
                console.log(res.data)
                this.mypie.setOption(this.setPieOptions(res.data))
                this.setTopicPercentage(res.data)
            })
        },
        getHistoryLog(){
            axios.get("http://localhost:8080//historyContent?log="+this.selectValue).then((res) => {
                console.log(res.data)
                this.history_log = res.data
            })
        },
        setTopicPercentage(data){
            const topic_option = [
                    {value: ((data['topic']['life'] / data['topic']['totalNum'])*100).toFixed(2), topic: '生活', label: '生活', count:data['topic']['life']},
                    {value: ((data['topic']['work']/ data['topic']['totalNum'])*100).toFixed(2), topic: '工作', label: '工作', count:data['topic']['work']},
                    {value: ((data['topic']['learn']/ data['topic']['totalNum'])*100).toFixed(2), topic: '学习', label: '学习', count:data['topic']['learn']},
                    {value: ((data['topic']['entertainment']/ data['topic']['totalNum'])*100).toFixed(2), topic: '娱乐', label: '娱乐', count:data['topic']['entertainment']}
            ]
            function sortId(a, b) {
                return b.value - a.value;//由高到低
            }
            topic_option.sort(sortId);
            this.top_topic = [
                {title: "第一主题：" + topic_option[0]['topic'] + "频次：" + topic_option[0]['count']},
                {title: "第二主题：" + topic_option[1]['topic'] + "频次：" + topic_option[1]['count']}
                ]
            this.topic_percentage = topic_option
        },
        setPieOptions(data){
            const series = []
            const legendData = {
                    orient: 'vertical',
                    left: 5,
                    data: ['宗教', '非法词汇', '敏感人名', '敏感新闻', '政治相关', '其他']
                }
            const options = {
                title: {
                    text: "敏感词类型分布",
                    left: "40%"
                },
                tooltip: {
                    trigger: 'item',
                    formatter: '{a} <br/>{b}: {c} ({d}%)',
                    show:true
                },
                legend: {
                    orient: 'vertical',
                    left: 5,
                    data: ['邪教', '非法词汇', '敏感人名', '不正当竞争', '政治、民族安全', '其他']
                },
                series: [
                    {
                        name: '敏感词类型分布',
                        type: 'pie',
                        radius: ['50%', '70%'],
                        avoidLabelOverlap: false,
                        label: {
                            show: true,
                            position: 'outside'
                        },
                        emphasis: {
                            label: {
                                show: true,
                                fontSize: '30',
                                fontWeight: 'bold'
                            }
                        },
                        labelLine: {
                            show: true
                        },
                        data: [
                            {value: data['sensitiveWords']['cult'], name: '邪教'},
                            {value: data['sensitiveWords']['illegalwords'], name: '非法词汇'},
                            {value: data['sensitiveWords']['names'], name: '敏感人名'},
                            {value: data['sensitiveWords']['news'], name: '不正当竞争'},
                            {value: data['sensitiveWords']['political'], name: '政治、民族安全'},
                            {value: data['sensitiveWords']['otherSenesitive'], name: '其他'}
                        ]
                    }]
            }
            return options
            }
        // handleListener() {
        //     bus.$on('collapse', this.handleBus);
        //     // 调用renderChart方法对图表进行重新渲染
        //     window.addEventListener('resize', this.renderChart);
        // },
        // handleBus(msg) {
        //     setTimeout(() => {
        //         this.renderChart();
        //     }, 200);
        // },
        // renderChart() {
        //     this.$refs.bar.renderChart();
        //     this.$refs.line.renderChart();
        // }
    }
};
</script>


<style scoped>
.el-row {
    margin-bottom: 20px;
}

.grid-content {
    display: flex;
    align-items: center;
    height: 100px;
}

.grid-cont-right {
    flex: 1;
    text-align: center;
    font-size: 14px;
    color: #999;
}

.grid-num {
    font-size: 30px;
    font-weight: bold;
}

.grid-con-icon {
    font-size: 50px;
    width: 100px;
    height: 100px;
    text-align: center;
    line-height: 100px;
    color: #fff;
}

.grid-con-1 .grid-con-icon {
    background: rgb(45, 140, 240);
}

.grid-con-1 .grid-num {
    color: rgb(45, 140, 240);
}

.grid-con-2 .grid-con-icon {
    background: rgb(100, 213, 114);
}

.grid-con-2 .grid-num {
    color: rgb(45, 140, 240);
}

.grid-con-3 .grid-con-icon {
    background: rgb(242, 94, 67);
}

.grid-con-3 .grid-num {
    color: rgb(242, 94, 67);
}

.user-info {
    display: flex;
    align-items: center;
    padding-bottom: 20px;
    border-bottom: 2px solid #ccc;
    margin-bottom: 20px;
}

.user-avator {
    width: 120px;
    height: 120px;
    border-radius: 50%;
}

.user-info-cont {
    padding-left: 50px;
    flex: 1;
    font-size: 14px;
    color: #999;
}

.user-info-cont div:first-child {
    font-size: 30px;
    color: #222;
}

.user-info-list {
    font-size: 14px;
    color: #999;
    line-height: 25px;
}

.user-info-list span {
    margin-left: 70px;
}

.mgb20 {
    margin-bottom: 20px;
}

.todo-item {
    font-size: 14px;
}

.todo-item-del {
    text-decoration: line-through;
    color: #999;
}

.schart{
    width: 100%;
    height: 300px;
}
</style>
