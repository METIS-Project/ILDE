AssessmentIcons = {
    "test": {
        src: "images/assessment/patterns/individualtest.png",
        emp: "images/assessment/patterns/individualtest_e.png",
        width: 252,
        height: 286
    },
    "observations": {
        src: "images/assessment/patterns/observations-collaborativework.png",
        emp: "images/assessment/patterns/observations-collaborativework_e.png",
        width: 337,
        height: 302
	    },
    "reportassessment": {
        src: "images/assessment/patterns/review-written-report.png",
        emp: "images/assessment/patterns/review-written-report_e.png",
        width: 249,
        height: 247
    },
    "oralpresentation": {
        src: "images/assessment/patterns/oral-presentation.png",
        emp: "images/assessment/patterns/oral-presentation_e.png",
        width: 267,
        height: 258
    },
    "performance": {
        src: "images/assessment/patterns/performance-assessment.png",
        emp: "images/assessment/patterns/performance-assessment_e.png",
        width: 273,
        height: 273
    },
    "portfolio": {
        src: "images/assessment/patterns/portfolio-assessment.png",
        emp: "images/assessment/patterns/portfolio-assessment_e.png",
        width: 266,
        height: 267
    },
    "roledetection": {
        src: "images/assessment/patterns/role-detection.png",
        emp: "images/assessment/patterns/role-detection_e.png",
        width: 293,
        height:282
    },
    "rubric": {
        src: "images/assessment/patterns/rubrics.png",
        emp: "images/assessment/patterns/rubrics_e.png",
        width: 256,
        height: 254
    },
    "sharedgrades": {
        src: "images/assessment/patterns/shared-grade.png",
        emp: "images/assessment/patterns/shared-grade_e.png",
        width: 271,
        height: 254
    },
    "selfassessment": {
        src: "images/assessment/patterns/self-assessment.png",
        emp: "images/assessment/patterns/self-assessment_e.png",
        width: 276,
        height: 253
    },
    "anonymous": {
        src: "images/assessment/patterns/anonymous-assessment.png",
        emp: "images/assessment/patterns/anonymous-assessment_e.png",
        width: 276,
        height: 253
    },
    "interactionanalisys": {
        src: "images/assessment/patterns/interaction-analysis.png",
        emp: "images/assessment/patterns/interaction-analysis_e.png",
        width: 337,
        height: 302
    },
    "random": {
        src: "images/assessment/patterns/random-member-assessment.png",
        emp: "images/assessment/patterns/random-member-assessment_e.png",
        width: 267,
        height: 256
    },
    /** functions **/
    "formative": {
        src: "images/assessment/links/feedback.png",
        emp: "images/assessment/links/feedback_e.png",
        width: 100,
        height: 100
    },
    "diagnosis": {
        src: "images/assessment/links/work.png",
        emp: "images/assessment/links/work_e.png",
        width: 157,
        height: 134
    },
    "assessed": {
        src: "images/assessment/links/assessed.png",
        emp: "images/assessment/links/assessed_e.png",
        width: 107,
        height: 102
    },
    "empty": {
        src: "images/assessment/empty.png",
        emp: "images/assessment/empty_e.png",
        width: 276,
        height: 255
    },

    getDataFixedWidth: function(name, width, scale, center) {
        var data = this[name];
        var k = scale * width / data.width;
        return {
            src: data.src,
            emp: data.emp,
            width: data.width * k,
            height: data.height * k,
            x: center.x - .5 * data.width * k,
            y: center.y - .5 * data.height * k
        };
    },

    getDataFixedWidth2: function(name, width, center) {
        var data = this[name];
        var k = width / data.width;
        return {
            src: data.src,
            emp: data.emp,
            width: data.width * k,
            height: data.height * k,
            x: center.x - .5 * data.width * k,
            y: center.y - .5 * data.height * k
        };
    },
    
    getData: function(name, scale, center) {
        var data = this[name];
        return {
            src: data.src,
            emp: data.emp,
            width: data.width * scale,
            height: data.height * scale,
            x: center.x - .5 * data.width * scale,
            y: center.y - .5 * data.height * scale
        };
    }
};


