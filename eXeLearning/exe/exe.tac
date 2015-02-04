import os
import sys
from exe.application import ExeService
from twisted.application import service

exeservice = ExeService()
application = service.Application("eXeLearning")
service.IProcess(application).processName = "exelearning"
exeservice.setServiceParent(application)
